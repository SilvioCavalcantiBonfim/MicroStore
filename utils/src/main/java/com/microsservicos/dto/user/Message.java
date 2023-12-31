package com.microsservicos.dto.user;

public final class Message {
    public static final String NAME_BLANK = "the name field must not be blank";
    public static final String CPF_BLANK = "the cpf field must not be blank";
    public static final String ADDRESS_BLANK = "the address field must not be blank";
    public static final String EMAIL_BLANK = "the email field must not be blank";
    public static final String PHONE_BLANK = "the phone field must not be blank";
    public static final String NAME_LENGTH = "the name must be between 1 and 100 characters long";
    public static final String CPF_LENGTH = "the CPF must be exactly 11 digits long";
    public static final String INVALID_EMAIL = "please enter a valid email address";

    private Message() {}
}
