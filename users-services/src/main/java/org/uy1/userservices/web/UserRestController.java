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
    public ResponseEntity<UsersDTO>createUsers(@RequestBody UsersDTO usersDTO){
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
    public UsersDTO updateUsers(@RequestBody UsersDTO usersDTO){
        try {
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

    @PostMapping("/privileges")
    public  ResponseEntity<PrivilegeDTO> addNewPrivilege(@RequestBody PrivilegeDTO privilegeDTO){
        try {
            PrivilegeDTO newPrivilege = usersService.addNewPrivilege(privilegeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPrivilege);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
