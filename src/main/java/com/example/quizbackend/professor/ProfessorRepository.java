package com.example.quizbackend.professor;

import com.example.quizbackend.admin.Admin;
import com.example.quizbackend.general.GeneralInfoUser;
import com.example.quizbackend.general.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<UserInfo, Long> {

    @Query("Select user from UserInfo user where user.email=?1")
    UserInfo findByEmail(String email);

    //Find professor from user by professor EMAIL
    @Query("SELECT s from UserInfo s where s.email=?1")
    Optional<UserInfo> findProfessorByEmail(String email);
    //Find professor by ID
    @Query("SELECT s from UserInfo s where s.id=?1")
    Optional<UserInfo> findProfessorByProfessor_id(Long professorID);

    Boolean existsByUsername(String username);

    @Query("Select u.username from UserInfo u where u.id=?1")
    String getUsernameById(Long professorId);

    Boolean existsByEmail(String email);

    //GetProfessorInfo according to projecting
    @Query("SELECT s from UserInfo s where s.username=?1")
    GeneralInfoUser getByUsernameGeneral(String username);

    @Query("SELECT s from UserInfo s where s.username=?1")
    UserInfo getByUsernameAllInfo(String username);

    @Query("SELECT s from UserInfo s where s.resetPasswordToken=?1")
    public UserInfo findByResetPasswordToken(String token);
    @Query (value = "select * from users_info a where a.user_id>0" ,nativeQuery = true)
    List<UserInfo> adminlist();

    @Query (value = "SELECT COUNT(ui) > 0 FROM UserInfo ui inner join ui.roles roles WHERE ui.id=?1 and roles.id = 3")
    Boolean existsByRoleAndId(Long id);
}
