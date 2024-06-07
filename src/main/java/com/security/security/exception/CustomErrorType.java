package com.security.security.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomErrorType {
    LocalDateTime timestamp;
    String message;
    List<String> errors;

    public CustomErrorType(AppSecurityException e) {
        this.timestamp = LocalDateTime.now();
        this.message = e.getMessage();
        this.errors = new ArrayList<>();
    }
}
