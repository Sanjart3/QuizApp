package com.example.quizbackend.questions.exams.qpaper;

import lombok.Data;

import java.util.List;

@Data

public class PostExamPapersAndQuestionListDTO {
    private ExamPapers examPapers;
    List<Long> questionID;
}
