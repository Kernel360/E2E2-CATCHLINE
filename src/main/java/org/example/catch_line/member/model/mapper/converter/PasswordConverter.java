package org.example.catch_line.member.model.mapper.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.catch_line.member.model.vo.Password;

@Converter(autoApply = true)
public class PasswordConverter implements AttributeConverter<Password, String> {

    @Override
    public String convertToDatabaseColumn(Password password) {
        return password.getEncodedPassword();
    }

    @Override
    public Password convertToEntityAttribute(String password) {
        return new Password(password);
    }
}
