package org.uy1.userservices.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "privileges")
public class Privilege{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long priId;
    private String priName;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "profile_privilege", joinColumns = @JoinColumn(name = "privilegeId"), inverseJoinColumns = @JoinColumn(name = "profileId"))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Profile> profiles;
}
