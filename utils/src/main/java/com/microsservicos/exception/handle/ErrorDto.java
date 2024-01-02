package com.microsservicos.exception.handle;

import java.time.LocalDateTime;

record ErrorDto(int status, String message, LocalDateTime timestamp) {
}
