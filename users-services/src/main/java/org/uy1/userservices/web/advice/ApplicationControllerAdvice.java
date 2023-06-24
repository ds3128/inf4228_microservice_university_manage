package org.uy1.userservices.web.advice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.uy1.userservices.dtos.ErrorEntity;
import org.uy1.userservices.exceptions.*;

@ControllerAdvice
public class ApplicationControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public @ResponseBody ErrorEntity handlerException(EntityNotFoundException exception){
        return new ErrorEntity(null, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateUserException.class)
    public @ResponseBody ErrorEntity handlerDuplicateUserException(DuplicateUserException exception){
        return new ErrorEntity(null, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateProfileException.class)
    public @ResponseBody ErrorEntity handlerDuplicateProfileException(DuplicateProfileException exception){
        return new ErrorEntity(null, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsersNotFoundException.class)
    public @ResponseBody ErrorEntity handlerUsersNotFoundException(UsersNotFoundException exception){
        return new ErrorEntity(null, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoMatchException.class)
    public @ResponseBody ErrorEntity handlerNoMatchException(NoMatchException exception){
        return new ErrorEntity(null, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public @ResponseBody ErrorEntity handlerException(Exception exception){
        return new ErrorEntity(null, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProfileNotFoundException.class)
    public @ResponseBody ErrorEntity handlerProfileNotFoundException(ProfileNotFoundException exception){
        return new ErrorEntity(null, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PrivilegeNotFoundException.class)
    public @ResponseBody ErrorEntity handlerPrivilegeNotFoundException(PrivilegeNotFoundException exception){
        return new ErrorEntity(null, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public @ResponseBody ErrorEntity handlerIllegalArgumentException(IllegalArgumentException exception){
        return new ErrorEntity(null, exception.getMessage());
    }
}
