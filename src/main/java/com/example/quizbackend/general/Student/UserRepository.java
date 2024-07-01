package com.example.quizbackend.general.Student;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.example.quizbackend.courses.CourseDTO;
import com.example.quizbackend.courses.Courses;
import com.example.quizbackend.general.GeneralInfoUser;
import com.example.quizbackend.general.UserInfo;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
  @Transactional
  @Modifying
  @Query("update UserInfo u set u.password = ?1, u.first_name = ?2")
  int updatePasswordAndFirst_nameBy(String password, String first_name);
  Optional<UserInfo> findByUsername(String username);
  Boolean existsByUsername(String username);
  Boolean existsUserInfoByUsername(String username);

  Boolean existsUserInfoByEmail(String email);

  UserInfo findUserInfoById(Long id);

  UserInfo findUserInfoByUsername(String username);

  Boolean existsByEmail(String email);
  @Query("SELECT s from UserInfo s where s.username=?1")
  GeneralInfoUser findProfessorByUsername(String username);
  @Query("SELECT s from UserInfo s where s.username=?1")
  Optional<UserInfo> findByUsernameInfo(String username);

  @Query("SELECT  s.coursesList from UserInfo s where s.id=?1")
  List<CourseDTO> getCourselistByID(Long id);
  @Query(value = "Select u.username from users_info u JOIN courses_user_infos c ON " +
          "u.user_id=c.user_infos_user_id " +
          "JOIN courses co ON co.course_id=c.courses_list_course_id where u.role='ROLE_PROFESSOR' AND c.courses_list_course_id=?1  ",nativeQuery = true)
  List<String> getStudentUsernames(Long id);

}
