package com.example.quizbackend.questions.exams.answertable;

import lombok.Data;

@Data
public class ExamAnswerSheetDTO {
    private Long exam_answer_sheet_id;
    private String answerText;
    private Boolean Response;
}
