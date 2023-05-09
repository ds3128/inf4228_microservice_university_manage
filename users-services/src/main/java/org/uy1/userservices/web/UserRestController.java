package org.uy1.userservices.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.uy1.userservices.dtos.PrivilegeDTO;
import org.uy1.userservices.dtos.ProfileDTO;
import org.uy1.userservices.dtos.UsersDTO;
import org.uy1.userservices.enums.ProfileName;
import org.uy1.userservices.exceptions.*;
import org.uy1.userservices.service.UsersServiceImpl;

import java.util.List;

@RestController
@Slf4j
@RefreshScope
@RequestMapping(path = "/api")
public class UserRestController {

    private UsersServiceImpl usersService;

    public UserRestController(UsersServiceImpl usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/users")
    public ResponseEntity<UsersDTO> createUsers(@RequestBody UsersDTO usersDTO){
        try {
            UsersDTO createUser = usersService.createUsers(usersDTO);
            return new ResponseEntity<>(createUser, HttpStatus.CREATED);
        } catch (DuplicateUserException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (NoMatchException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{userId}")
    public UsersDTO updateUsers(@PathVariable Long userId, @RequestBody UsersDTO usersDTO){
        try {
            usersDTO.setUserId(userId);
            return usersService.updateUsers(usersDTO);
        } catch (UsersNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Users not found", e);
        } catch (DuplicateUserException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exist", e);
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        try {
            usersService.deleteUsers(userId);
            return ResponseEntity.ok("User with id " + userId + " has been deleted.");
        } catch (UsersNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/search?firstName={firstName}")
    public List<UsersDTO> findByUsersName(@PathVariable String firstName){
        try {
            List<UsersDTO> users = usersService.findUserByName(firstName);
            return users;
        } catch (UsersNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<?> loadUserByUsername(@PathVariable String username){
        try {
            UsersDTO users = usersService.loadUserByUsername(username);
            return ResponseEntity.ok(users);
        } catch (UsersNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/profiles")
    public ResponseEntity<?> addNewProfile(@RequestBody ProfileDTO profileDTO){
        try {
            ProfileDTO saveProfile = usersService.addNewProfile(profileDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(saveProfile);
        } catch (DuplicateProfileException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("profile already exist");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error income during creating profile");
        }
    }

    @PostMapping("/users/{username}/profile/{profileName}")
    public  ResponseEntity<?> addProfileToUser(@PathVariable String username, @PathVariable ProfileName profileName){
        try {
            usersService.addProfileToUser(username, profileName);
            return ResponseEntity.ok().build();
        } catch (UsersNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (ProfileNotFoundException e){
            return ResponseEntity.badRequest().body("Profile not found");
        }
    }

    @DeleteMapping("/users/{username}/profile/{profileName}")
    public void removeProfileToUser(@PathVariable String username, @PathVariable ProfileName profileName) throws UsersNotFoundException{
        try {
            usersService.removeProfileFromUser(username, profileName);
        } catch (UsersNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    @GetMapping("/profiles")
    public ResponseEntity<List<ProfileDTO>> getAllProfile(){
        try {
            List<ProfileDTO> profileDTOS = usersService.getAllProfile();
            return ResponseEntity.ok(profileDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/profiles/{profileId}")
    public ResponseEntity<ProfileDTO> getProfileById(@PathVariable Long profileId){
        try {
            ProfileDTO profileDTO = usersService.getProfileById(profileId);
            return ResponseEntity.ok(profileDTO);
        } catch (ProfileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/profiles/{profileName}")
    public List<UsersDTO> getUsersByProfileName(@PathVariable ProfileName profileName){
        return usersService.getUsersByProfileType(profileName);
    }
    @PutMapping("/profiles/{profileId}")
    public ResponseEntity<ProfileDTO> updateProfile(@PathVariable Long profileId, @RequestBody ProfileDTO profileDTO){
        try {
            profileDTO.setProfileId(profileId);
            ProfileDTO updateProfile = usersService.updateProfile(profileDTO);
            return new ResponseEntity<>(updateProfile, HttpStatus.OK);
        } catch (ProfileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/profiles/{profileId}")
    public ResponseEntity<Void> removeProfileById(@PathVariable Long profileId){
        try {
            usersService.removeProfileById(profileId);
            return ResponseEntity.noContent().build();
        } catch (ProfileNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/privileges")
    public  ResponseEntity<PrivilegeDTO> addNewPrivilege(@RequestBody PrivilegeDTO privilegeDTO){
        try {
            PrivilegeDTO newPrivilege = usersService.addNewPrivilege(privilegeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPrivilege);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/profiles/{profileId}/privileges")
    public ResponseEntity<String> addPrivilegeToProfile(@PathVariable Long profileId, @RequestBody PrivilegeDTO privilegeDTO) {
        try {
            ProfileDTO profileDTO = usersService.getProfileById(profileId);
            usersService.addPrivilegeToProfile(profileDTO, privilegeDTO);
            return ResponseEntity.ok("Privilege added successfully to profile !");
        } catch (ProfileNotFoundException | PrivilegeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding privilege to profile.");
        }
    }
    @DeleteMapping("/profiles/{profileId}/privileges/{privilegeId}")
    public ResponseEntity<?> removePrivilegeToProfile(@PathVariable Long profileId, @PathVariable Long privilegeId) {
        try {
            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setProfileId(profileId);
            PrivilegeDTO privilegeDTO = new PrivilegeDTO();
            privilegeDTO.setPriId(privilegeId);
            usersService.removePrivilegeToProfile(profileDTO, privilegeDTO);
            return ResponseEntity.ok().build();
        } catch (ProfileNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (PrivilegeNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/privileges")
    public ResponseEntity<List<PrivilegeDTO>> getAllPrivilege(){
        try {
            List<PrivilegeDTO> privilegeDTOS = usersService.getAllPrivileges();
            return ResponseEntity.ok(privilegeDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/privileges/{privilegesId}")
    public ResponseEntity<PrivilegeDTO> updatePrivilege(@PathVariable Long privilegesId, @RequestBody PrivilegeDTO privilegeDTO) {
        try {
            privilegeDTO.setPriId(privilegesId);
            PrivilegeDTO updatePrivilege = usersService.updatePrivilege(privilegeDTO);
            return ResponseEntity.ok().body(updatePrivilege);
        } catch (PrivilegeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/privileges/{privilegesId}")
    public ResponseEntity<?> removePrivilege(@PathVariable Long privilegesId){
        try {
            usersService.removePrivilege(privilegesId);
            return new ResponseEntity<>("Privilege removed successfully", HttpStatus.OK);
        } catch (PrivilegeNotFoundException p) {
            return new ResponseEntity<>(p.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/profiles/{profileId}/privileges")
    public ResponseEntity<List<PrivilegeDTO>> getPrivilegeByProfile(@PathVariable Long profileId){
        try {
            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setProfileId(profileId);
            List<PrivilegeDTO> privilegeDTOS = usersService.getPrivilegesByProfile(profileDTO);
            return ResponseEntity.ok(privilegeDTOS);
        } catch (ProfileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
