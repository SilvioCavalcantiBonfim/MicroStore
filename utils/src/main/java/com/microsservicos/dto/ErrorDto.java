package com.microsservicos.dto;

import java.util.Date;

public record ErrorDto(int status, String message, Date timestamp) {
}
