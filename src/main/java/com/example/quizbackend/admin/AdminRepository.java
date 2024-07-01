package com.example.quizbackend.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//TODO admin repository should be there, Main propose for admin repository to check from table if
//admin credentials are correct or not.
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findAdminById(Long id);

    Admin findAdminByUsername(String username);

    void deleteAdminById(Long id);

    Admin findAdminByEmail(String email);

    Boolean existsAdminByEmail(String email);

    Boolean existsAdminByUsername(String name);

    @Query (value = "select * from admin a where a.id>0" ,nativeQuery = true)
    List<Admin> adminlist();
}
