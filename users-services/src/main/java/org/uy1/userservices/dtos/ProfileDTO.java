package org.uy1.userservices.dtos;

import lombok.Data;
import org.uy1.userservices.entities.Privilege;
import org.uy1.userservices.entities.Users;
import org.uy1.userservices.enums.ProfileName;

import java.util.List;

@Data
public class ProfileDTO {
    private Long profileId;
    private ProfileName profileName;
    private List<Privilege> privilegeList;
    private List<Users> usersList;
}
