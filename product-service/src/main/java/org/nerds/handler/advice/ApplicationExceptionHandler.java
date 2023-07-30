package org.nerds.handler.advice;

import org.nerds.handler.exception.ConflictException;
import org.nerds.handler.exception.NoDataFoundException;
import org.nerds.utils.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleInvalidArgumentException(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", new Date().toString());
        error.put("errorMessage", "Provided Invalid Arguments");
        Map<String, String> reason = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> reason.put(fieldError.getField(), fieldError.getDefaultMessage()));
        error.put("reason", reason);
        error.put("errorCode", 40001);
        return error;
    }

    @ExceptionHandler({NoDataFoundException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNoDataException(Exception error) {
        Map<String, Object> ex = new LinkedHashMap<>();
        ex.put("timestamp", new Date().toString());
        if (error instanceof NoDataFoundException)
            ex.put("errorMessage", "No data found");
        else if (error instanceof IllegalArgumentException) {
            String message = error.getMessage();
            if (Utility.isStringEmptyOrNull(message)) message = "Provided illegeal arguments";
            ex.put("errorMessage", message);
        } else {
            ex.put("errorMessage", "Unexpected Condition");
        }
        ex.put("errorCode", 40401);
        return ex;
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleConflictException(ConflictException ex) {
        final String message = ex.getMessage();
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", new Date().toString());
        error.put("errorMessage", "Conflict occurred");
        if (Utility.isStringNonEmpty(message)) {
            error.put("reason", message);
        }
        error.put("errorCode", 40901);
        return error;
    }
}
