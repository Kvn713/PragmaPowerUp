package co.com.pragma.crediya.model.controlledexceptions.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    EXIST_EMAIL("EE1", "Email already exists"),
    USER_NOT_FOUND("NF1", "User Not Found"),
    EMAIL_NOT_FOUND("NF2", "Email not found"),
    TOKEN_NOT_FOUND("BT1", "Token not found"),
    BAD_TOKEN("BT2", "Bad token"),
    BAD_TOKEN_STUCTURE("BT3", "Invalid Authentication structure"),
    BAD_CREDENTIALS("BC1", "Bad Credentials"),
    UNAUTHORIZED_ROLE("UR1", "Unauthorized role for this action"),
    DEFAULT_EXCEPTION("", "Generic exception, Not Found");

    private final String code;
    private final String message;

    public static ErrorMessage errorMessage(String value) {
        return Arrays.stream(ErrorMessage.values())
                .filter(errorMessage -> errorMessage.getCode().contains(value))
                .findAny()
                .orElse(ErrorMessage.DEFAULT_EXCEPTION);
    }
}
