package com.design.utils;

import com.design.entity.base.BaseEntity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

public class CommonUtil {

    // 移除重複的值
    public static <T> List<T> removeDuplicates(List<T> list){
        if(null == list || list.isEmpty()){
            return list;
        }
        LinkedHashSet<T> set = new LinkedHashSet<>(list);
        return new ArrayList<>(set);
    }

    // 透過uuid取得物件
    public static <T extends BaseEntity> T getEntityByUuid(List<T> list, UUID uuid){
        if(null == list || list.isEmpty()){
            return null;
        }
        for(T t : list){
            if(t.getUuid().equals(uuid)){
                return t;
            }
        }
        return null;
    }

    // 取得uuid清單
    public static <T extends BaseEntity> List<UUID> getEntityUuids(List<T> list){
        List<UUID> uuids = new ArrayList<>();
        if(null == list || list.isEmpty()){
            return uuids;
        }
        for(T t : list){
            uuids.add(t.getUuid());
        }
        return uuids;
    }

}
