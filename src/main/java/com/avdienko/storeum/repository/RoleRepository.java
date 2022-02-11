package com.avdienko.storeum.repository;

import com.avdienko.storeum.model.entity.ERole;
import com.avdienko.storeum.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
