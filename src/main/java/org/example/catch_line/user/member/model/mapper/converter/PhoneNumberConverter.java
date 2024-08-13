package org.example.catch_line.user.member.model.mapper.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.catch_line.user.member.model.vo.PhoneNumber;

@Converter(autoApply = true)
public class PhoneNumberConverter implements AttributeConverter<PhoneNumber, String> {


    @Override
    public String convertToDatabaseColumn(PhoneNumber phoneNumber) {
        return phoneNumber.getPhoneNumberValue();
    }

    @Override
    public PhoneNumber convertToEntityAttribute(String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }
}