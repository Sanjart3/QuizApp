package com.example.quizbackend.questions.exams.questionlist;

import com.example.quizbackend.questions.exams.qpaper.ExamPapers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;


@Repository
public interface QuestionListRepository extends JpaRepository<QuestionList,Long> {
    List<QuestionList> findAllByExamPapers(ExamPapers examPapers);
    @Query(value = "SELECT  e.* from question_list e where e.exam_papers_id=?1 and e.q_type='WRITTEN'",nativeQuery = true)
    List<QuestionList> findAll(Long id);
}