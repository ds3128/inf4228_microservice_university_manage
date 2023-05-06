package org.uy1.userservices.exceptions;

public class DuplicateProfileException extends RuntimeException {
    public DuplicateProfileException(String message){
        super(message);
    }
}
