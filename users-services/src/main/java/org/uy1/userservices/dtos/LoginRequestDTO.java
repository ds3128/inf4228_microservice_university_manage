package org.uy1.userservices.dtos;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}
