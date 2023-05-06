package org.uy1.userservices.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    private Date birthDate;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String numTel;
    @Column(unique = true)
    private String username;
    private String password;
//    @Lob
//    private byte[] pictures;
    @ManyToOne
    private Profile profile;
}
