package org.example.catch_line.common.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.catch_line.common.model.vo.Email;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email.getEmailValue();
    }

    @Override
    public Email convertToEntityAttribute(String email) {
        return new Email(email);
    }
}
