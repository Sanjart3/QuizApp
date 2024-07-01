package com.example.quizbackend.questions;


import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import java.util.List;
import java.util.Map;

import com.example.quizbackend.questions.exams.answertable.ExamAnswerSheetDTO;
import lombok.Data;


@Data
public class   RandomQuestionDTO {
    private Long id;
    @Enumerated(EnumType.STRING)
    private QType qType;
    @Column(length = 1000)
    private String question;
    @Lob
    private String photo;
    private Double questionPoint;

    private List<ExamAnswerSheetDTO> examAnswerSheetDTO;
}
