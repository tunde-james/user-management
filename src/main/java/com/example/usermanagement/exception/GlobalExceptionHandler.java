package com.example.usermanagement.exception;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<FieldErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDetail(
                        error.getField(), Objects.requireNonNullElse(error.getDefaultMessage(), "Invalid value")))
                .toList();

        ProblemDetail problemDetail = ApiProblemDetails.validationError(request.getRequestURI(), errors);

        return ApiProblemDetails.response(HttpStatus.BAD_REQUEST, problemDetail);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFoundException(
            UserNotFoundException ex, HttpServletRequest request) {

        ProblemDetail problemDetail = ApiProblemDetails.notFound(
                request.getRequestURI(), "user-not-found", "User not found", ex.getMessage());

        return ApiProblemDetails.response(HttpStatus.NOT_FOUND, problemDetail);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex, HttpServletRequest request) {

        ProblemDetail problemDetail = ApiProblemDetails.conflict(
                request.getRequestURI(), "user-already-exists", "User already exists", ex.getMessage());

        return ApiProblemDetails.response(HttpStatus.CONFLICT, problemDetail);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ProblemDetail> handleInvalidPasswordException(
            InvalidPasswordException ex, HttpServletRequest request) {

        ProblemDetail problemDetail = ApiProblemDetails.unauthorized(
                request.getRequestURI(), "invalid-password", "Invalid password", ex.getMessage());

        return ApiProblemDetails.response(HttpStatus.UNAUTHORIZED, problemDetail);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Invalid username/email or password");
        problemDetail.setType(URI.create("https://moviebookingapp/problems/bad-credentials"));
        problemDetail.setTitle("Authentication failed");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ApiProblemDetails.response(HttpStatus.UNAUTHORIZED, problemDetail);
    }
}
