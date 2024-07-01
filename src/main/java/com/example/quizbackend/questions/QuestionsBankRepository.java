package com.example.quizbackend.questions;

import com.example.quizbackend.general.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface QuestionsBankRepository extends JpaRepository<QuestionsBank, Long> {
    @Query("SELECT s FROM QuestionsBank s where s.courseID.course_id=?1")
    List<QuestionsBank> findAllQuestionByCourseID(Long courseID);
    @Query("SELECT s from QuestionsBank s where s.question_id=:question_id ")
    QuestionsBank findByQuestion_id(@Param("question_id") Long question_id);
    @Query(value = "SELECT  * FROM questions q WHERE q.question_course=?1 and q.status=false and q.q_type=?2 " + "ORDER BY RANDOM() LIMIT ?3  ",nativeQuery = true)
    List<QuestionsBank> findAllQuestionsForPaper(Long question_course,String q_type,Long numberOfQuestions);
    @Query(value = "SELECT  * FROM questions q WHERE q.question_course=?1 and q.status=false " + "ORDER BY RANDOM() LIMIT ?2  ",nativeQuery = true)
    List<QuestionsBank> findAllRandomQuestions(Long question_course,Long numberOfQuestions);

}