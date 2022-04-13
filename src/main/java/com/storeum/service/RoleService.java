package com.storeum.service;

import com.storeum.exception.ResourceNotFoundException;
import com.storeum.model.entity.ERole;
import com.storeum.model.entity.Role;
import com.storeum.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;

    public Role getRoleByName(ERole role) {
        return repository.findByName(role).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Role with value=%s was not found in DB", role.name()))
        );
    }
}
