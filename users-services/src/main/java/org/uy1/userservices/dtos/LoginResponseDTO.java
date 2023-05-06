package org.uy1.userservices.dtos;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private UsersDTO user;
}
