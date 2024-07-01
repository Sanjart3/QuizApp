package com.example.quizbackend.questions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface AnswersRepository extends JpaRepository<Answers, Long> {
    //@Query("DELETE s FROM Answers  LECT s FROM QuestionsBank s where s.courseID.course_id=?1")
    @Query("Select a FROM Answers a where a.questionsBank.question_id=:question_id")
    List<Answers> findAllByQuestionId(@Param("question_id") Long question_id);
    @Query(value = "INSERT INTO  answers(question_id) Values(id) where ",nativeQuery = true)
    void  addQID(@Param("id") Long id);
}