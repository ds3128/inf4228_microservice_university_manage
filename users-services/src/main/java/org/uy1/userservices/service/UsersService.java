package org.uy1.userservices.service;

import org.uy1.userservices.dtos.PrivilegeDTO;
import org.uy1.userservices.dtos.ProfileDTO;
import org.uy1.userservices.dtos.UsersDTO;
import org.uy1.userservices.enums.ProfileName;
import org.uy1.userservices.exceptions.PrivilegeNotFoundException;

import java.util.List;

public interface UsersService {

    UsersDTO createUsers(UsersDTO usersDTO);

    UsersDTO updateUsers(Long userId, UsersDTO usersDTO);

    void deleteUsers(Long userId);
    List<UsersDTO> findUserByName(String firstName);
    UsersDTO loadUserByUsername(String username);

    ProfileDTO addNewProfile(ProfileDTO profileDTO);

    void addProfileToUser(String username, ProfileName profileName);

    void removeProfileFromUser(String username, ProfileName role);

    PrivilegeDTO addNewPrivilege(PrivilegeDTO privilegeDTO);

    void addPrivilegeToUser(String username, String privilege) throws PrivilegeNotFoundException;

    void removePrivilegeFromUser(String username, String privilege) throws PrivilegeNotFoundException;
}
