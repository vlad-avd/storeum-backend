package com.avdienko.storeum.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class HttpUtil {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> ResponseEntity<?> okOrNotFound(Optional<T> entity, String body) {
        if (entity.isPresent()) {
            return ResponseEntity.ok(entity);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }
    }
}
