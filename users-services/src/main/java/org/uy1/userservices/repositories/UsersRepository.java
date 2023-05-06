package org.uy1.userservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uy1.userservices.entities.Users;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    List<Users> findByFirstName(String firstName);
    boolean existsByUserId(Long userId);
    boolean existsByUsername(String username);

}
