package com.digiyak.shared;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception translator ({@code @RestControllerAdvice}) across all Spring Modulith modules.
 *
 * <p><b>Why RFC 7807 {@link ProblemDetail}?</b>
 * Spring 6+ / Boot 4 natively supports RFC 7807 ("Problem Details for HTTP APIs"), giving every error
 * response a consistent JSON shape ({@code type}, {@code title}, {@code status}, {@code detail},
 * plus custom properties like {@code errorCode} and {@code timestamp}) instead of inconsistent error payloads.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Handles business/domain exceptions thrown via {@link ApiException}. */
    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        problem.setType(URI.create("https://digiyak.app/errors/" + ex.getErrorCode().toLowerCase()));
        problem.setTitle(ex.getErrorCode());
        problem.setProperty("errorCode", ex.getErrorCode());
        problem.setProperty("timestamp", Instant.now().toString());
        return problem;
    }

    /** Handles Jakarta Bean Validation failures ({@code @Valid}) and maps each invalid field to its message. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        err -> err.getDefaultMessage() != null ? err.getDefaultMessage() : "Invalid value",
                        (first, second) -> first // Keep first error if a field fails multiple constraints
                ));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request validation failed"
        );
        problem.setType(URI.create("https://digiyak.app/errors/validation_failed"));
        problem.setTitle("VALIDATION_FAILED");
        problem.setProperty("errorCode", "VALIDATION_FAILED");
        problem.setProperty("fields", fieldErrors);
        problem.setProperty("timestamp", Instant.now().toString());
        return problem;
    }

    /** Fallback handler for invalid method arguments (e.g. malformed UUIDs or currency codes). */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("INVALID_ARGUMENT");
        problem.setProperty("errorCode", "INVALID_ARGUMENT");
        problem.setProperty("timestamp", Instant.now().toString());
        return problem;
    }
}
