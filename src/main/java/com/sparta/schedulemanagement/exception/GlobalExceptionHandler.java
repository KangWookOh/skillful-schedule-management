package com.sparta.schedulemanagement.exception;

import com.sparta.schedulemanagement.common.response.ApiResponse;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(InvalidCredentialsException ex) {
        log.error("인증 실패: {}", ex.getMessage());
        ApiResponse<String> response = ApiResponse.error("AUTH_FAILED", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(IllegalArgumentException ex) {
        ApiResponse<String> response = ApiResponse.error("AUTH_FAILED", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException e) {
        log.error("런타임 예외: {}", e.getMessage());
        ApiResponse<String> response = ApiResponse.error("UNAUTHORIZED", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ApiResponse<String>> handleServletException(ServletException e) {
        log.error("서블릿 예외: {}", e.getMessage());
        ApiResponse<String> response = ApiResponse.error("BAD_REQUEST", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException e) {
        log.error("접근 거부: {}", e.getMessage());
        ApiResponse<String> response = ApiResponse.error("FORBIDDEN", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
