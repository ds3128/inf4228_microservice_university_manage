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
import org.uy1.userservices.exceptions.*;
import org.uy1.userservices.mappers.PrivilegeMapper;
import org.uy1.userservices.mappers.ProfileMapper;
import org.uy1.userservices.mappers.UsersMapper;
import org.uy1.userservices.repositories.PrivilegeRepository;
import org.uy1.userservices.repositories.ProfileRepository;
import org.uy1.userservices.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
            throw new DuplicateUserException("UserId already exist");
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
    public UsersDTO updateUsers(UsersDTO usersDTO) throws UsersNotFoundException {
        log.info("updating user");
        Users users = usersRepository.findById(usersDTO.getUserId()).orElseThrow(()-> new UsersNotFoundException("user id not exist"));
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
        usersRepository.findById(userId).orElseThrow(() -> new UsersNotFoundException("user with id : " + userId + " not exist"));
        usersRepository.deleteById(userId);
    }



    @Override
    public UsersDTO loadUserByUsername(String username) {
        log.info("loading user");
        Users users = usersRepository.findByUsername(username);
        return usersMapper.convertToUserDTO(users);
    }
    @Override
    public List<UsersDTO> findUserByName(String firstName) {
        log.info("finding user");
        List<Users> user = usersRepository.findByFirstName(firstName);
        List<UsersDTO> usersDTO = user.stream().map(users -> usersMapper.convertToUserDTO(users)).collect(Collectors.toList());
        return usersDTO;
    }


    @Override
    public List<UsersDTO> getUsersByProfileType(ProfileName profileName) {
        List<Users> users = new ArrayList<>();
        usersRepository.findByProfile(profileName);
        List<UsersDTO> usersDTOS = users.stream().map(usr -> usersMapper.convertToUserDTO(usr)).collect(Collectors.toList());
        return usersDTOS;
    }

    @Override
    public List<UsersDTO> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        //Version programmation fonctionnelle
        List<UsersDTO> usersDTOS = users.stream().map(usr -> usersMapper.convertToUserDTO(usr)).collect(Collectors.toList());
        //Version programmation imperative : classique
//        List<UsersDTO> usersDTOS = new ArrayList<>();
//        for (Users users1 : users){
//            UsersDTO usersDTO = usersMapper.convertToUserDTO(users1);
//            usersDTOS.add(usersDTO);
//        }
        return usersDTOS;
    }

    @Override
    public UsersDTO getUsersById(Long userId) {
        Users users = usersRepository.findById(userId).orElseThrow(() -> new UsersNotFoundException("Users Id not exist"));
        return usersMapper.convertToUserDTO(users);
    }
    @Override
    public List<ProfileDTO> getAllProfile() {
        List<Profile> profiles = profileRepository.findAll();
        List<ProfileDTO> profileDTOS = profiles.stream().map(profile -> profileMapper.convertToProfileDTO(profile)).collect(Collectors.toList());
        return profileDTOS;
    }

    @Override
    public ProfileDTO getProfileById(Long profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException("Profile id not found"));
        return profileMapper.convertToProfileDTO(profile);
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
    public ProfileDTO updateProfile(ProfileDTO profileDTO) {
        Profile profile = profileRepository.findById(profileDTO.getProfileId()).orElseThrow(() -> new ProfileNotFoundException("Profile id not found"));
        profile.setProfileName(profileDTO.getProfileName());
        return profileMapper.convertToProfileDTO(profile);
    }

    @Override
    public void removeProfileById(Long profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException("Profile id not found"));
        profileRepository.delete(profile);
    }

    @Override
    public void addProfileToUser(String username, ProfileName profileName) {
        if (username.isEmpty() || profileName == null)
            throw new UsersNotFoundException("username and password invalid");
        Users users = usersRepository.findByUsername(username);
        if (users == null){
            throw new UsersNotFoundException("User not found");
        }
        Profile profile = profileRepository.findByProfileName(profileName);
        if (profile == null)
            throw new ProfileNotFoundException("Profile not recognise");
        users.setProfile(profile);
    }

    @Override
    public void removeProfileFromUser(String username, ProfileName profileName) {
        Users users = usersRepository.findByUsername(username);
        if (users == null){
            throw new UsersNotFoundException("User not found");
        }
        Profile profile = profileRepository.findByProfileName(profileName);
        profile.getUsers().remove(users);
        users.setProfile(null);
        usersRepository.save(users);
    }

    @Override
    public void addPrivilegeToProfile(ProfileDTO profileDTO, PrivilegeDTO privilegeDTO) throws PrivilegeNotFoundException {
        if (profileDTO == null || profileDTO.getProfileId() == null || privilegeDTO == null || privilegeDTO.getPriId() == null)
            throw new IllegalArgumentException("profile and privilege most not be null");
        Profile profile = profileRepository.findById(profileDTO.getProfileId()).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        Privilege privilege = privilegeRepository.findById(privilegeDTO.getPriId()).orElseThrow(() -> new PrivilegeNotFoundException("Privilege not fount"));

        if (profile.getPrivileges().contains(privilege)){
            throw new IllegalArgumentException("Profile already has this privilege");
        }

        profile.getPrivileges().add(privilege);
        privilege.getProfiles().add(profile);

        profileRepository.save(profile);
        privilegeRepository.save(privilege);
    }

//    @Override
//    public void addPrivilegeToProfileWithoutDTO(Long profileId, Long privilegeId) throws PrivilegeNotFoundException {
//        Profile profile = profileRepository.findById(privilegeId).orElseThrow(() -> new ProfileNotFoundException("Profile id not found"));
//        Privilege privilege = privilegeRepository.findById(privilegeId).orElseThrow(() -> new PrivilegeNotFoundException("Privilege not found"));
//
//        profile.getPrivileges().add(privilege);
//        privilege.getProfiles().add(profile);
//
//        profileRepository.save(profile);
//        privilegeRepository.save(privilege);
//    }

    @Override
    public void removePrivilegeToProfile(ProfileDTO profileDTO, PrivilegeDTO privilegeDTO) throws PrivilegeNotFoundException {
        Profile profile = profileRepository.findById(profileDTO.getProfileId()).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        Privilege privilege = privilegeRepository.findById(privilegeDTO.getPriId()).orElseThrow(() -> new PrivilegeNotFoundException("Privilege not found"));

        if (!profile.getPrivileges().contains(privilege)) {
            throw new IllegalArgumentException("Profile does not have this privilege");
        }

        profile.getPrivileges().remove(privilege);
        privilege.getProfiles().remove(profile);

        profileRepository.save(profile);
        privilegeRepository.save(privilege);
    }

//    @Override
//    public void removePrivilegeToProfileWithoutDTO(Long profileId, Long privilegeId) throws PrivilegeNotFoundException {
//        Profile profile = profileRepository.findById(privilegeId).orElseThrow(() -> new ProfileNotFoundException("Profile id not found"));
//        Privilege privilege = privilegeRepository.findById(privilegeId).orElseThrow(() -> new PrivilegeNotFoundException("Privilege not found"));
//
//        privilege.getProfiles().remove(profile);
//        privilegeRepository.save(privilege);
//    }

    @Override
    public List<PrivilegeDTO> getAllPrivileges() {
        List<Privilege> privileges = privilegeRepository.findAll();
        //Version programmation fonctionnelle
        //List<PrivilegeDTO> privilegeDTOS = privileges.stream().map(privilege -> privilegeMapper.convertToPrivilegeDTO(privilege)).collect(Collectors.toList());
        List<PrivilegeDTO> privilegeDTOS = privileges.stream().map(privilegeMapper::convertToPrivilegeDTO).collect(Collectors.toList());
        return privilegeDTOS;
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
    public PrivilegeDTO updatePrivilege(PrivilegeDTO privilegeDTO) throws PrivilegeNotFoundException {
        log.info("Updating privilege");
        Privilege privilege = privilegeRepository.findById(privilegeDTO.getPriId()).orElseThrow(() -> new PrivilegeNotFoundException("Privilege not found"));
        privilege.setPriName(privilegeDTO.getPriName());
        Privilege updatePrivilege = privilegeRepository.save(privilege);
        return privilegeMapper.convertToPrivilegeDTO(updatePrivilege);
    }

    @Override
    public void removePrivilege(Long privilegeId) throws PrivilegeNotFoundException {
        privilegeRepository.findById(privilegeId).orElseThrow(() -> new PrivilegeNotFoundException("Privilege Id not found"));
        privilegeRepository.deleteById(privilegeId);
    }

    @Override
    public List<PrivilegeDTO> getPrivilegesByProfile(ProfileDTO profileDTO) {
        if (profileDTO == null || profileDTO.getProfileId() == null){
            throw new ProfileNotFoundException("Profile cannot be null");
        }
        Profile profile = profileRepository.findById(profileDTO.getProfileId()).orElseThrow(() -> new ProfileNotFoundException("profile not found"));
        List<PrivilegeDTO> privilegeDTOS = new ArrayList<>();
        for (Privilege privilege : profile.getPrivileges()){
            PrivilegeDTO privilegeDTO = new PrivilegeDTO();
            privilegeDTO.setPriName(privilege.getPriName());
            privilegeDTOS.add(privilegeDTO);
        }
        return privilegeDTOS;
    }

}
