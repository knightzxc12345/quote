package com.quote.filter;

import com.quote.base.Common;
import com.quote.base.eunms.AuthEnum;
import com.quote.service.user_detail.UserDetailsService;
import com.quote.utils.HttpUtil;
import com.quote.utils.JwtUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        String authorHeader = request.getHeader(Common.HEADER_STRING);
        if(StringUtils.isBlank(authorHeader) || !authorHeader.startsWith(Common.TOKEN_PREFIX)){
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorHeader.substring(Common.TOKEN_PREFIX.length());
        String userName = getUserName(token);
        if(StringUtils.isBlank(userName)){
            HttpUtil.write(AuthEnum.A00006);
            return;
        }
        // 取得UserDetail
        if(null != userName && null == SecurityContextHolder.getContext().getAuthentication()){
            UserDetails userDetails = userDetailsService.loadUserById(userName);
            if(!JwtUtil.isTokenExpired(token)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getUserName(final String token){
        try{
            return JwtUtil.extractUsername(token);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

}
