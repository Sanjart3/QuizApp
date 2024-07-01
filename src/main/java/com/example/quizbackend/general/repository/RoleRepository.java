package com.example.quizbackend.general.repository;

import java.util.Optional;

import com.example.quizbackend.general.ERole;
import com.example.quizbackend.general.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
