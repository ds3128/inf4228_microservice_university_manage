package org.uy1.userservices.dtos;

import lombok.Data;
import org.uy1.userservices.entities.Profile;

import java.util.Date;

@Data
public class UsersDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;
    private String numTel;
    private String username;
    private String password;
    private String confirmPassword;
    private Profile profile;
}
