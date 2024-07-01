package com.example.quizbackend.courses.syllabus;

import com.example.quizbackend.courses.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {






    @Query("SELECT s FROM syllabus s where s.courses.course_id=?1")
    List<Syllabus> getAllByCourseID(Long courseID);



}