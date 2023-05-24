package com.tfg.sprintplannerapi.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


    @Setter
    @Getter
    @RequiredArgsConstructor
    @NoArgsConstructor
    public class ErrorDTO {
        @NonNull
        private HttpStatus statusCode;

        @NonNull
        private String mensaje;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
        private LocalDateTime fecha = LocalDateTime.now();

    }

