package org.uy1.userservices.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uy1.userservices.dtos.PrivilegeDTO;
import org.uy1.userservices.dtos.ProfileDTO;
import org.uy1.userservices.dtos.UsersDTO;
import org.uy1.userservices.entities.Privilege;
import org.uy1.userservices.entities.Profile;
import org.uy1.userservices.entities.Users;
import org.uy1.userservices.enums.ProfileName;
import org.uy1.userservices.exceptions.DuplicateUserException;
import org.uy1.userservices.exceptions.NoMatchException;
import org.uy1.userservices.exceptions.PrivilegeNotFoundException;
import org.uy1.userservices.exceptions.UsersNotFoundException;
import org.uy1.userservices.mappers.PrivilegeMapper;
import org.uy1.userservices.mappers.ProfileMapper;
import org.uy1.userservices.mappers.UsersMapper;
import org.uy1.userservices.repositories.PrivilegeRepository;
import org.uy1.userservices.repositories.ProfileRepository;
import org.uy1.userservices.repositories.UsersRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UsersServiceImpl implements UsersService {

    private UsersRepository usersRepository;
    private ProfileRepository profileRepository;
    private PrivilegeRepository privilegeRepository;
    private PrivilegeMapper privilegeMapper;
    private ProfileMapper profileMapper;
    private UsersMapper usersMapper;

    //private PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository, ProfileRepository profileRepository, PrivilegeRepository privilegeRepository, PrivilegeMapper privilegeMapper, ProfileMapper profileMapper, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.profileRepository = profileRepository;
        this.privilegeRepository = privilegeRepository;
        this.privilegeMapper = privilegeMapper;
        this.profileMapper = profileMapper;
        this.usersMapper = usersMapper;
        //this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsersDTO createUsers(UsersDTO usersDTO) throws DuplicateUserException {
        log.info("Saving user");
        if (usersRepository.existsByUserId(usersDTO.getUserId()))
            throw new DuplicateUserException("Username already exist");
        if (usersRepository.existsByUsername(usersDTO.getUsername()))
            throw new DuplicateUserException("Username already exist");
        if (!usersDTO.getPassword().equals(usersDTO.getConfirmPassword()))
            throw new NoMatchException("The password dose not match");
        Users users = Users.builder()
                .firstName(usersDTO.getFirstName())
                .lastName(usersDTO.getLastName())
                .birthDate(usersDTO.getBirthDate())
                .email(usersDTO.getEmail())
                .numTel(usersDTO.getNumTel())
                .username(usersDTO.getUsername())
                .password(usersDTO.getPassword())
                .build();
        Users saveUser = usersRepository.save(users);
        return usersMapper.convertToUserDTO(saveUser);
    }

    @Override
    public UsersDTO updateUsers(Long userId, UsersDTO usersDTO) throws UsersNotFoundException {
        log.info("updating user");
        Users users = usersRepository.findById(userId).orElseThrow(()-> new UsersNotFoundException("user id : " + userId + " not exist"));
        if (usersRepository.existsByUserId(usersDTO.getUserId()))
            throw new DuplicateUserException("Username already exist");
        if (usersRepository.existsByUsername(usersDTO.getUsername()))
            throw new DuplicateUserException("Username already exist");
        users.setFirstName(usersDTO.getFirstName());
        users.setLastName(usersDTO.getLastName());
        users.setBirthDate(usersDTO.getBirthDate());
        users.setEmail(usersDTO.getEmail());
        users.setNumTel(usersDTO.getNumTel());
        users.setUsername(usersDTO.getUsername());
        Users updateUser = usersRepository.save(users);
        return usersMapper.convertToUserDTO(updateUser);
    }

    @Override
    public void deleteUsers(Long userId) {
        log.info("deleting user");
        usersRepository.findById(userId).orElseThrow(()-> new UsersNotFoundException("user id : " + userId + " not exist"));
        usersRepository.deleteById(userId);
    }

    @Override
    public List<UsersDTO> findUserByName(String firstName) {
        log.info("finding user");
        List<Users> usersList = usersRepository.findByFirstName(firstName);
        List<UsersDTO> usersDTO = usersList.stream().map(users -> usersMapper.convertToUserDTO(users)).collect(Collectors.toList());
        return usersDTO;
    }

    @Override
    public UsersDTO loadUserByUsername(String username) {
        log.info("loading user");
        Users users = usersRepository.findByUsername(username);
        return usersMapper.convertToUserDTO(users);
    }

    @Override
    public ProfileDTO addNewProfile(ProfileDTO profileDTO) {
        log.info("adding profile");
        Profile profile = Profile.builder()
                .profileName(profileDTO.getProfileName())
                .build();
        Profile saveProfile = profileRepository.save(profile);
        return profileMapper.convertToProfileDTO(saveProfile);
    }

    @Override
    public void addProfileToUser(String username, ProfileName profileName) {
        Users users = usersRepository.findByUsername(username);
        if (users == null){
            throw new UsersNotFoundException("User not found");
        }
        Profile profile = profileRepository.findByProfileName(String.valueOf(profileName));
        users.setProfile(profile);
        usersRepository.save(users);
    }

    @Override
    public void removeProfileFromUser(String username, ProfileName profileName) {
        Users users = usersRepository.findByUsername(username);
        if (users == null){
            throw new UsersNotFoundException("User not found");
        }
        Profile profile = profileRepository.findByProfileName(String.valueOf(profileName));
        users.getProfile().getUsers().remove(users);
        profile.getUsers().remove(users);
        usersRepository.save(users);
        profileRepository.save(profile);
    }

    @Override
    public PrivilegeDTO addNewPrivilege(PrivilegeDTO privilegeDTO) {
        Privilege privilege = Privilege.builder()
                .priName(privilegeDTO.getPriName())
                .build();
        Privilege savePrivilege = privilegeRepository.save(privilege);
        return privilegeMapper.convertToPrivilegeDTO(savePrivilege);
    }

    @Override
    public void addPrivilegeToUser(String username, String privilege) throws PrivilegeNotFoundException {
        Users users = usersRepository.findByUsername(username);
        if (users == null){
            throw new UsersNotFoundException("User not found");
        }
        Privilege priv = privilegeRepository.findByPriName(privilege);
        if (priv == null){
            throw new PrivilegeNotFoundException("Privilege not found");
        }
        List<Privilege> privileges = users.getProfile().getPrivileges();
        if (privileges != null){
            privileges.add(priv);
        }
        usersRepository.save(users);
    }

    @Override
    public void removePrivilegeFromUser(String username, String privilege) throws PrivilegeNotFoundException {
        Users users = usersRepository.findByUsername(username);
        if (users == null){
            throw new UsersNotFoundException("User not found");
        }
        Privilege priv = privilegeRepository.findByPriName(privilege);
        if (priv == null){
            throw new PrivilegeNotFoundException("Privilege not found");
        }
        List<Privilege> privileges = users.getProfile().getPrivileges();
        if (privileges != null){
            privileges.remove(priv);
        }
        usersRepository.save(users);
    }
}
