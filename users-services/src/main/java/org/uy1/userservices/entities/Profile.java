package org.uy1.userservices.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uy1.userservices.enums.ProfileName;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    @Enumerated(EnumType.STRING)
    private ProfileName profileName;
    @ManyToMany(fetch = FetchType.EAGER )
    @JoinTable(name = "profile_privilege", joinColumns = @JoinColumn(name = "privilegeId"), inverseJoinColumns = @JoinColumn(name = "profileId"))
    private List<Privilege> privileges;
    @OneToMany(mappedBy = "profile", fetch = FetchType.EAGER)
    private List<Users> users;
}
