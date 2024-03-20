package com.design.utils;

import java.util.List;

public class PageUtil {

    public static <T> List<T> getPage(List<T> list, int page, int size) {
        if(list.size() <= size){
            return list;
        }
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, list.size());
        return list.subList(startIndex, endIndex);
    }

}
