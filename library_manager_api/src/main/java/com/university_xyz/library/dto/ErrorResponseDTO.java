package com.university_xyz.library.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorResponseDTO {
    private String message;
    private LocalDateTime timestamp;
    private List<String> validationErrors;

    public ErrorResponseDTO(String message, LocalDateTime timestamp){
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorResponseDTO(String message, LocalDateTime timestamp,List<String> validationErrors){
        this.message = message;
        this.timestamp = timestamp;
        this.validationErrors = validationErrors;
    }
}
