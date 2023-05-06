package org.uy1.userservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uy1.userservices.entities.Privilege;

import java.util.List;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByPriName(String namePrivilege);
    List<Privilege> getPrivilegeByPriName(String name);
}
