package com.example.quizbackend.questions.exams.answertable;

import com.example.quizbackend.questions.exams.questionlist.QuestionList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
@Transactional
public interface ExamAnswerSheetRepository extends JpaRepository<ExamAnswerSheet, Long> {

    List<ExamAnswerSheet> findAllByQuestionList(QuestionList questionList);
    @Query(value = "SELECT e.* from exam_answer_sheet e where e.question_list_id=?1 and e.response is null ",nativeQuery = true)
    ExamAnswerSheet findWritten(Long id);

    @Query(value = "Select e.* from exam_answer_sheet e where e.answer_id=?1 ",nativeQuery = true)
    ExamAnswerSheet findByID(Long id);
    @Query(value = "SELECT e.* from exam_answer_sheet e where e.question_list_id=?1 ",nativeQuery = true)
    ExamAnswerSheet findByQuestionId(Long id);

}