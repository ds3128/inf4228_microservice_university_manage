package org.uy1.userservices.repositories;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.uy1.userservices.entities.Users;
import org.uy1.userservices.enums.ProfileName;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
    @Query("SELECT u FROM Users u WHERE u.firstName =: firstName")
    List<Users> findByFirstName(@Param(("firstName")) String firstName);
    @Query("SELECT u from Users u where u.profile =: pro")
    List<Users> findByProfile(@Param(("pro")) ProfileName profileName);

    boolean existsByUserId(Long userId);

    boolean existsByUsername(String username);
}
