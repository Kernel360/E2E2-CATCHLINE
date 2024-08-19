package org.example.catch_line.common.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.catch_line.common.model.vo.PhoneNumber;

@Converter(autoApply = true)
public class PhoneNumberConverter implements AttributeConverter<PhoneNumber, String> {


    @Override
    public String convertToDatabaseColumn(PhoneNumber phoneNumber) {

        if(phoneNumber == null) {return null;}

        return phoneNumber.getPhoneNumberValue();
    }

    @Override
    public PhoneNumber convertToEntityAttribute(String phoneNumber) {
        if(phoneNumber == null) {return null;}
        return new PhoneNumber(phoneNumber);
    }
}
