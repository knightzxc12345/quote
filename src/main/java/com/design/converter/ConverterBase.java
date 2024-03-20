package com.design.converter;

import com.design.entity.enums.EnumBase;
import jakarta.persistence.AttributeConverter;

public abstract class ConverterBase<T extends Enum<T> & EnumBase<K>, K> implements AttributeConverter<T, K> {

    private final Class<T> clazz;

    public ConverterBase(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public K convertToDatabaseColumn(T attribute) {
        return attribute != null ? (K) attribute.get() : null;
    }

    @Override
    public T convertToEntityAttribute(K value) {
        T[] enums = clazz.getEnumConstants();
        for (T e : enums) {
            if (e.get().equals(value)) {
                return e;
            }
        }
        return null;
    }

}
