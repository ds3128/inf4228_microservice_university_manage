package org.uy1.userservices.dtos;

import lombok.Builder;
import lombok.Data;
import org.uy1.userservices.entities.Profile;

import java.util.List;

@Data
public class PrivilegeDTO {
    private Long priId;
    private String priName;
    private List<Profile> profileList;
}
