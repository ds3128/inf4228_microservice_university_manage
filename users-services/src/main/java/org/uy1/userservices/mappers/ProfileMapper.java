package org.uy1.userservices.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.uy1.userservices.dtos.ProfileDTO;
import org.uy1.userservices.entities.Profile;

@Service
public class ProfileMapper {
    private ModelMapper modelMapper = new ModelMapper();
    public ProfileDTO convertToProfileDTO(Profile profile){
        return modelMapper.map(profile, ProfileDTO.class);
    }

    public Profile convertToProfile(ProfileDTO profileDTO){
        return modelMapper.map(profileDTO, Profile.class);
    }
}
