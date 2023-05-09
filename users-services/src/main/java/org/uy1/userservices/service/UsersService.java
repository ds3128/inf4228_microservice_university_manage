package org.uy1.userservices.service;

import org.uy1.userservices.dtos.PrivilegeDTO;
import org.uy1.userservices.dtos.ProfileDTO;
import org.uy1.userservices.dtos.UsersDTO;
import org.uy1.userservices.enums.ProfileName;
import org.uy1.userservices.exceptions.PrivilegeNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UsersService {

    //INTERFACE SERVICE FOR USERS
    UsersDTO createUsers(UsersDTO usersDTO);
    UsersDTO updateUsers(UsersDTO usersDTO);
    void deleteUsers(Long userId);
    List<UsersDTO> findUserByName(String firstName);
    List<UsersDTO> getUsersByProfileType(ProfileName profileName);
    List<UsersDTO> getAllUsers();
    UsersDTO getUsersById(Long userId);
    UsersDTO loadUserByUsername(String username);


    //INTERFACE SERVICE FOR PROFILE
    List<ProfileDTO> getAllProfile();
    ProfileDTO getProfileById(Long profileId);
    ProfileDTO addNewProfile(ProfileDTO profileDTO);
    ProfileDTO updateProfile(ProfileDTO profileDTO);
    void removeProfileById(Long profileId);
    void addProfileToUser(String username, ProfileName profileName);
    void removeProfileFromUser(String username, ProfileName role);
    void addPrivilegeToProfile(ProfileDTO profileDTO, PrivilegeDTO privilegeDTO) throws PrivilegeNotFoundException;

//    void addPrivilegeToProfileWithoutDTO(Long profileId, Long privilegeId) throws PrivilegeNotFoundException;

    void removePrivilegeToProfile(ProfileDTO profileDTO, PrivilegeDTO privilegeDTO) throws PrivilegeNotFoundException;


//    void removePrivilegeToProfileWithoutDTO(Long profileId, Long privilegeId) throws PrivilegeNotFoundException;

    //INTERFACE SERVICE FOR PRIVILEGE
    List<PrivilegeDTO> getAllPrivileges();
    PrivilegeDTO addNewPrivilege(PrivilegeDTO privilegeDTO);
    PrivilegeDTO updatePrivilege(PrivilegeDTO privilegeDTO) throws PrivilegeNotFoundException;
    void removePrivilege(Long privilegeId) throws PrivilegeNotFoundException;
    List<PrivilegeDTO> getPrivilegesByProfile(ProfileDTO profileDTO);

}
