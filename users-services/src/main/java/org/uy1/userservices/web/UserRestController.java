package org.uy1.userservices.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.uy1.userservices.dtos.PrivilegeDTO;
import org.uy1.userservices.dtos.ProfileDTO;
import org.uy1.userservices.dtos.UsersDTO;
import org.uy1.userservices.enums.ProfileName;
import org.uy1.userservices.exceptions.*;
import org.uy1.userservices.service.UsersServiceImpl;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RefreshScope
@RequestMapping(path = "/api")
public class UserRestController {

    private UsersServiceImpl usersService;

    public UserRestController(UsersServiceImpl usersService){
        this.usersService = usersService;
    }

    @PostMapping("/addUsers")
    public UsersDTO createUsers(@RequestBody UsersDTO usersDTO) {
        return usersService.createUsers(usersDTO);
    }

    @GetMapping("/users")
    public List<UsersDTO> getAllUsers() {
        return usersService.getAllUsers();
    }

    @PutMapping("/users/{userId}")
    public UsersDTO updateUsers(@PathVariable Long userId, @RequestBody UsersDTO usersDTO){
        usersDTO.setUserId(userId);
        return usersService.updateUsers(usersDTO);
    }

    @DeleteMapping("/users/delete/{userId}")
    public void deleteUser(@PathVariable Long userId){
        usersService.deleteUsers(userId);
    }

    @GetMapping("/users/search/firstName")
    public List<UsersDTO> findByUsersName(@PathVariable String firstName){
        return usersService.findUserByName(firstName);
    }

    @GetMapping("/users/{username}")
    public UsersDTO loadUserByUsername(@PathVariable String username){
            return usersService.loadUserByUsername(username);
    }

    @PostMapping("/profiles")
    public ProfileDTO addNewProfile(@RequestBody ProfileDTO profileDTO){
        return usersService.addNewProfile(profileDTO);
    }

    @PostMapping("/users/{username}/profile/{profileName}")
    public  void addProfileToUser(@PathVariable String username, @PathVariable ProfileName profileName){
        this.usersService.addProfileToUser(username, profileName);
    }

    @DeleteMapping("/users/{username}/profile/{profileName}")
    public void removeProfileToUser(@PathVariable String username, @PathVariable ProfileName profileName) throws UsersNotFoundException{
        usersService.removeProfileFromUser(username, profileName);
    }
    @GetMapping("/profiles")
    public List<ProfileDTO> getAllProfile(){

        return usersService.getAllProfile();
    }
    @GetMapping("/profiles/{profileId}")
    public ProfileDTO getProfileById(@PathVariable Long profileId){

        return usersService.getProfileById(profileId);
    }

    @GetMapping(path = "/users/profiles/{profileName}", produces = APPLICATION_JSON_VALUE)
    public List<UsersDTO> getUsersByProfileName(@PathVariable ProfileName profileName){
        return usersService.getUsersByProfileType(profileName);
    }
    @PutMapping(path = "/profiles/{profileId}", consumes = APPLICATION_JSON_VALUE)
    public ProfileDTO updateProfile(@PathVariable Long profileId, @RequestBody ProfileDTO profileDTO){

        profileDTO.setProfileId(profileId);
        return usersService.updateProfile(profileDTO);
    }

    @DeleteMapping("/profiles/{profileId}")
    public void removeProfileById(@PathVariable Long profileId){

        this.usersService.removeProfileById(profileId);
    }

    @PostMapping(path = "/privileges", consumes = APPLICATION_JSON_VALUE)
    public  PrivilegeDTO addNewPrivilege(@RequestBody PrivilegeDTO privilegeDTO){

        return usersService.addNewPrivilege(privilegeDTO);
    }
    @PostMapping("/profiles/{profileId}/privileges")
    public void addPrivilegeToProfile(@PathVariable Long profileId, @RequestBody PrivilegeDTO privilegeDTO) throws PrivilegeNotFoundException {
        ProfileDTO profileDTO = usersService.getProfileById(profileId);
        usersService.addPrivilegeToProfile(profileDTO, privilegeDTO);
    }
    @DeleteMapping("/profiles/{profileId}/privileges/{privilegeId}")
    public void removePrivilegeToProfile(@PathVariable Long profileId, @PathVariable Long privilegeId) throws PrivilegeNotFoundException {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setProfileId(profileId);
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setPriId(privilegeId);
        usersService.removePrivilegeToProfile(profileDTO, privilegeDTO);
    }

    @GetMapping("/privileges")
    public List<PrivilegeDTO> getAllPrivilege(){

        return usersService.getAllPrivileges();
    }
    @PutMapping("/privileges/{privilegesId}")
    public PrivilegeDTO updatePrivilege(@PathVariable Long privilegesId, @RequestBody PrivilegeDTO privilegeDTO) throws PrivilegeNotFoundException {
        privilegeDTO.setPriId(privilegesId);
        return usersService.updatePrivilege(privilegeDTO);
    }
    @DeleteMapping("/privileges/{privilegesId}")
    public void removePrivilege(@PathVariable Long privilegesId) throws PrivilegeNotFoundException {
        usersService.removePrivilege(privilegesId);
    }
    @GetMapping("/profiles/{profileId}/privileges")
    public List<PrivilegeDTO> getPrivilegeByProfile(@PathVariable Long profileId){
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setProfileId(profileId);
        return usersService.getPrivilegesByProfile(profileDTO);
    }

}
