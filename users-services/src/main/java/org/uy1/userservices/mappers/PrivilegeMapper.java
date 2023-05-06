package org.uy1.userservices.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.uy1.userservices.dtos.PrivilegeDTO;
import org.uy1.userservices.entities.Privilege;

@Service
public class PrivilegeMapper {
    ModelMapper modelMapper = new ModelMapper();
    public PrivilegeDTO convertToPrivilegeDTO(Privilege privilege){
        return modelMapper.map(privilege, PrivilegeDTO.class);
    }

    public Privilege convertToPrivilege(PrivilegeDTO privilegeDTO){
        return modelMapper.map(privilegeDTO, Privilege.class);
    }
}
