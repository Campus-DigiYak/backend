package com.digiyak.shared;

import org.springframework.http.HttpStatus;

/**
 * Standard domain/API exception translated by {@link GlobalExceptionHandler} into an RFC 7807 ProblemDetail.
 *
 * <p><b>Why a single custom RuntimeException?</b>
 * Rather than letting controllers return ad-hoc error strings or catching checked exceptions everywhere,
 * any service or domain entity can throw {@code ApiException} with an HTTP status, a machine-readable
 * {@code errorCode} (for the Next.js frontend to handle programmatically), and a human-readable message.
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus status;
    private final String errorCode;

    public ApiException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    // Static factory helpers keep call sites readable and enforce consistent HTTP status codes:

    /** 400 Bad Request — malformed input or invalid business parameter (e.g. negative fee). */
    public static ApiException badRequest(String errorCode, String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, errorCode, message);
    }

    /** 401 Unauthorized — missing, expired, tampered, or invalid credentials/token. */
    public static ApiException unauthorized(String message) {
        return new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", message);
    }

    /** 403 Forbidden — authenticated user lacks the required role or campus scope. */
    public static ApiException forbidden(String errorCode, String message) {
        return new ApiException(HttpStatus.FORBIDDEN, errorCode, message);
    }

    /** 404 Not Found — requested entity ID or tracking/short code does not exist. */
    public static ApiException notFound(String errorCode, String message) {
        return new ApiException(HttpStatus.NOT_FOUND, errorCode, message);
    }

    /** 409 Conflict — duplicate account, idempotency clash, or invalid state transition. */
    public static ApiException conflict(String errorCode, String message) {
        return new ApiException(HttpStatus.CONFLICT, errorCode, message);
    }

    /** 422 Unprocessable Content — syntactically valid input rejected by business rules (e.g. wrong PIN). */
    public static ApiException unprocessable(String errorCode, String message) {
        return new ApiException(HttpStatus.UNPROCESSABLE_CONTENT, errorCode, message);
    }
}
