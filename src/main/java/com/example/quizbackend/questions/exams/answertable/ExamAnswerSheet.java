package com.example.quizbackend.questions.exams.answertable;

import com.example.quizbackend.questions.Answers;
import com.example.quizbackend.questions.exams.questionlist.QuestionList;
import lombok.Data;

import javax.persistence.*;
@Entity
@Data
public class ExamAnswerSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id", nullable = false)
    private Long exam_answer_sheet_id;
    private String answerText;
    private Boolean CorrectA;
    private Boolean Response;
    private Integer orderNumber;
    @ManyToOne
    @JoinColumn(name = "question_list_id")
    private QuestionList questionList;


}
