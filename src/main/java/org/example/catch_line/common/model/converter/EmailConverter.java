package org.example.catch_line.common.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.catch_line.common.model.vo.Email;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {

        if(email == null) {return null;}

        // TODO : null 값 검증을 request dto, Email vo, converter에서 하고 있는데 검증이 너무 많을까요?
        return email.getEmailValue();
    }

    @Override
    public Email convertToEntityAttribute(String email) {
        if(email == null) {return null;}

        return new Email(email); // TODO : 이렇게 VO 객체를 생성하면 괜찮을까요?
    }
}
