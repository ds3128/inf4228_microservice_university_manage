package org.uy1.userservices;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.uy1.userservices.dtos.ProfileDTO;
import org.uy1.userservices.dtos.UsersDTO;
import org.uy1.userservices.entities.Profile;
import org.uy1.userservices.entities.Users;
import org.uy1.userservices.enums.ProfileName;
import org.uy1.userservices.mappers.ProfileMapper;
import org.uy1.userservices.mappers.UsersMapper;
import org.uy1.userservices.repositories.ProfileRepository;
import org.uy1.userservices.repositories.UsersRepository;
import org.uy1.userservices.service.UsersServiceImpl;

import java.util.Date;

@SpringBootApplication
public class UsersServicesApplication {

    public static void main(String[] args) {

        SpringApplication.run(UsersServicesApplication.class, args);
    }

    //@Bean
    CommandLineRunner start(UsersServiceImpl usersService, ProfileRepository profileRepository, ProfileMapper profileMapper, UsersMapper usersMapper, UsersRepository usersRepository){
        return args -> {

            //insert profile user in data base
            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setProfileName(ProfileName.ETUDIANT);
            profileDTO.setProfileName(ProfileName.ENSEIGNANT);
            profileDTO.setProfileName(ProfileName.ADMINISTRATEUR);
            profileDTO.setProfileName(ProfileName.CHEF_DEPARTEMENT);
            profileDTO.setProfileName(ProfileName.SECRETAIRE);
            Profile profile = profileMapper.convertToProfile(profileDTO);
            profileRepository.save(profile);

            //inserting users
            UsersDTO usersDTO = new UsersDTO();
            usersDTO.setFirstName("Temgoua");
            usersDTO.setLastName("Darius");
            usersDTO.setBirthDate(new Date());
            usersDTO.setEmail("dariussteves@yahoo.fr");
            usersDTO.setNumTel("696378743");
            usersDTO.setUsername("ds3128");
            usersDTO.setPassword("12345");
            usersDTO.setConfirmPassword("12345");
            Users users = usersMapper.convertToUser(usersDTO);
            usersRepository.save(users);
        };
    }

}
