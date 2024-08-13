package org.example.catch_line.user.member.model.mapper.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.catch_line.user.member.model.vo.Email;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {

        // TODO : null 값 검증을 request dto, Email vo, converter에서 하고 있는데 검증이 너무 많을까요?
        return email.getEmailValue();
    }

    @Override
    public Email convertToEntityAttribute(String email) {
        return new Email(email); // TODO : 이렇게 VO 객체를 생성하면 괜찮을까요?
    }
}
