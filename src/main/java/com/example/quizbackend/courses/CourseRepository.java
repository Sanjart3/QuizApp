package com.example.quizbackend.courses;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Courses,Long > {

    @Query("select new com.example.quizbackend.courses.CourseDTO(c.course_id,c.name, c.code) from Courses c inner join c.userInfos userInfos where userInfos.id = ?1")
    List<CourseDTO> getCourseNameAndIdAndCode(Long id);
    @Query("SELECT s FROM Courses s where s.username=?1")
    List<Courses> getCoursesByUsername(String username);
    @Query("SELECT s from Courses  s where s.course_id=?1")
    List<Courses> getCoursesByCourse_id(Long professorID);

    @Query("delete from Courses c where c.course_id=:courseID")
    void deleteByCourse_id(Long courseID);

    @Query("SELECT COUNT(u) > 0 FROM Courses u WHERE u.username = :username AND u.course_id = :courseId")
    Boolean existsByUsernameAndCourse_id(String username, Long courseId);

    @Query("SELECT COUNT(c) > 0 FROM Courses c inner join c.userInfos ui inner join ui.roles roles WHERE (c.course_id = :courseId AND ui.username = :username) AND (roles.id=3 or roles.id=5)")
    boolean existsByCourse_idAndAndUserInfos(Long courseId, String username);

    @Query("SELECT COUNT(c) > 0 FROM Courses c inner join c.userInfos ui inner join ui.roles roles WHERE c.course_id=?1 and roles.id = 3")
    boolean existsByIdAndRole(Long id);

    @Query("SELECT c FROM Courses c")
    List<CourseDTO> getAllCourses();
}
