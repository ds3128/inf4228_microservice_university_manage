package org.uy1.userservices.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.uy1.userservices.dtos.UsersDTO;
import org.uy1.userservices.entities.Users;

@Service
public class UsersMapper {
    private ModelMapper modelMapper = new ModelMapper();

    public UsersDTO convertToUserDTO(Users users){

        return modelMapper.map(users, UsersDTO.class);
    }

    public Users convertToUser(UsersDTO usersDTO){

        return modelMapper.map(usersDTO, Users.class);
    }
}
