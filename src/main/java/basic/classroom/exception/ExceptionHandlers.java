package basic.classroom.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        ValidationErrorResponse errorResponse = ValidationErrorResponse.builder().status(400).errors(errors).build();

        log.error("Error Messages = {}", errorResponse.getErrors());

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ValidationErrorResponse> handleValidationException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        violations.stream().forEach((violation) -> {
                    String fieldName = violation.getPropertyPath().toString();
                    String errorMessage = violation.getMessage();
                    errors.put(fieldName, errorMessage);
                });
        ValidationErrorResponse errorResponse = ValidationErrorResponse.builder().status(400).errors(errors).build();

        log.error("Error Messages = {}", errorResponse.getErrors());

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(CreateDuplicatedMemberException.class)
    protected ResponseEntity<ErrorResponse> handleCreateDuplicatedMemberException(CreateDuplicatedMemberException e) {
        ErrorResponse errorResponse = ErrorResponse.fromErrorCode(e.getErrorCode());
        log.error("Error Message = {}", errorResponse.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidMemberStatusException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidMemberStatusException(InvalidMemberStatusException e) {
        ErrorResponse errorResponse = ErrorResponse.fromErrorCode(e.getErrorCode());
        log.error("Error message = {}", errorResponse.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.fromErrorCode(e.getErrorCode());
        log.error("Error message = {}", errorResponse.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(StoreImageException.class)
    protected ResponseEntity<ErrorResponse> handleStoreImageException(StoreImageException e) {
        ErrorResponse errorResponse = ErrorResponse.fromErrorCode(e.getErrorCode());
        log.error("Error message = {}", errorResponse.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(ConvertImageException.class)
    protected ResponseEntity<ErrorResponse> handleConvertImageException(ConvertImageException e) {
        ErrorResponse errorResponse = ErrorResponse.fromErrorCode(e.getErrorCode());
        log.error("Error message = {}", errorResponse.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(ApplyLectureException.class)
    protected ResponseEntity<ErrorResponse> handleApplyLectureException(ApplyLectureException e) {
        ErrorResponse errorResponse = ErrorResponse.fromErrorCode(e.getErrorCode());
        log.error("Error message = {}", errorResponse.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(UpdateLectureException.class)
    protected ResponseEntity<ErrorResponse> handleUpdateLectureException(UpdateLectureException e) {
        ErrorResponse errorResponse = ErrorResponse.fromErrorCode(e.getErrorCode());
        log.error("Error message = {}", errorResponse.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(LectureNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleLectureNotFoundException(LectureNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.fromErrorCode(e.getErrorCode());
        log.error("Error message = {}", errorResponse.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(AuthorizationNotAvailableException.class)
    protected ResponseEntity<ErrorResponse> handleAuthorizationNotAvailableException(AuthorizationNotAvailableException e) {
        ErrorResponse errorResponse = ErrorResponse.fromErrorCode(e.getErrorCode());
        log.error("Error message = {}", errorResponse.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(UpdateMemberException.class)
    protected ResponseEntity<ErrorResponse> handleUpdateMemberException(UpdateMemberException e) {
        ErrorResponse errorResponse = ErrorResponse.fromErrorCode(e.getErrorCode());
        log.error("Error message = {}", errorResponse.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(LectureSearchParameterException.class)
    protected ResponseEntity<ErrorResponse> handleLectureSearchParameterException(LectureSearchParameterException e) {
        ErrorResponse errorResponse = ErrorResponse.fromErrorCode(e.getErrorCode());
        log.error("Error message = {}", errorResponse.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }
}
