package org.uy1.userservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uy1.userservices.entities.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByProfileName(String nameProfile);
}
