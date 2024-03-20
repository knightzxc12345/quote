package com.design.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NativeUtil {

    private static ObjectMapper mapper;

    public static <T> List<T> Convert(final List<Map<String, Object>> list, final Class<T> clz){
        final List<T> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            resultList.add(Convert(map, clz));
        }
        return resultList;
    }

    public static <T> T Convert(final Map<String, Object> map, final Class<T> clz){
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return mapper.convertValue(map, clz);
    }

}
