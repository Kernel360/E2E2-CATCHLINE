package org.example.catch_line.common.model.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.catch_line.exception.phone.InvalidPhoneNumberException;

import java.util.regex.Pattern;

@Getter
@ToString
@EqualsAndHashCode
public class PhoneNumber {

    private final String phoneNumberValue;
    private static final String PHONE_NUMBER_REGEX = "^\\d{2,4}-\\d{3,4}-\\d{4}$";
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);


    public PhoneNumber(String phoneNumberValue) {
        validatePhoneNumber(phoneNumberValue);
        this.phoneNumberValue = phoneNumberValue;
    }

    private void validatePhoneNumber(String phoneNumberValue) {

        if (!PHONE_NUMBER_PATTERN.matcher(phoneNumberValue).matches()) {
            throw new InvalidPhoneNumberException(phoneNumberValue);
        }


    }


}
