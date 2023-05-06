package org.uy1.userservices.exceptions;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(String message){
        super(message);
    }
}
