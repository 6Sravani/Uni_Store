package com.uni_store.store.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record ErrorDto (
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
){}
