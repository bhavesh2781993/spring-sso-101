package in.digiborn.security.exceptions.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import in.digiborn.security.exceptions.UnauthorizedException;
import in.digiborn.security.model.Error;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public Error handleUnauthorizedException(UnauthorizedException unauthorizedException) {
        log.error("Unauthorized exception: ", unauthorizedException);
        return new Error(unauthorizedException.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Error handleRuntimeException(RuntimeException runtimeException) {
        log.error("Runtime exception: ", runtimeException);
        return new Error(runtimeException.getMessage());
    }

}
