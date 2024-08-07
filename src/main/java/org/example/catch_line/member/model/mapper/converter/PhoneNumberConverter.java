package org.example.catch_line.member.model.mapper.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.catch_line.exception.phone.InvalidPhoneNumberException;
import org.example.catch_line.member.model.vo.PhoneNumber;

@Converter(autoApply = true)
public class PhoneNumberConverter implements AttributeConverter<PhoneNumber, String> {


    @Override
    public String convertToDatabaseColumn(PhoneNumber phoneNumber) {
        if (phoneNumber == null) throw new InvalidPhoneNumberException();
        return phoneNumber.getPhoneNumberValue();
    }

    @Override
    public PhoneNumber convertToEntityAttribute(String phoneNumber) {
        if (phoneNumber == null) throw new InvalidPhoneNumberException();
        return new PhoneNumber(phoneNumber);
    }
}
