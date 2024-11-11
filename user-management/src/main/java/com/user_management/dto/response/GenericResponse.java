package com.user_management.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class GenericResponse<T> {
    @JsonFormat(pattern="dd-MMM-yyyy HH:mm:ss a", timezone="Asia/Kolkata")
    private LocalDateTime timestamp;
    private String path;
    private int status;
    private boolean success;
    private String message;
    private String error;
    private T data;

   public static <T> GenericResponse<T> build(String path, int status, boolean isSuccess, String message, String error, T data) {
        return GenericResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .path(path)
                .status(status)
                .success(isSuccess)
                .message(message)
                .error(error)
                .data(data)
                .build();
    }


}