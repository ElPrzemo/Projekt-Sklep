package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long>  {
}
