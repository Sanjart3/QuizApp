package com.example.quizbackend.questions.exams.questionlist;

import com.example.quizbackend.questions.Answers;
import com.example.quizbackend.questions.QType;
import com.example.quizbackend.questions.exams.answertable.ExamAnswerSheet;
import com.example.quizbackend.questions.exams.qpaper.ExamPapers;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
public class QuestionList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long choosen_question_id;
    private String questionText;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private ExamPapers examPapers;
    @OneToMany
    List<ExamAnswerSheet> answersList;
    @Enumerated(EnumType.STRING)
    private QType qType;
    private Double questionPoint;
    private Double timeToSpend;
    @Lob
    private String photo;
    private Integer questionOrderNumber;








}
