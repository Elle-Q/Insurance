package com.fintech.insurance.commons.enums.converter;

import com.fintech.insurance.commons.enums.RefundStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/17 0017 11:26
 */
@Converter
public class RefundStatusConverter implements AttributeConverter<RefundStatus, String> {

    @Override
    public String convertToDatabaseColumn(RefundStatus attribute) {
        if (attribute != null) {
            return attribute.getCode();
        }
        return null;
    }

    @Override
    public RefundStatus convertToEntityAttribute(String dbData) {
        if (dbData != null) {
            return RefundStatus.codeOf(dbData);
        }
        return null;
    }
}
