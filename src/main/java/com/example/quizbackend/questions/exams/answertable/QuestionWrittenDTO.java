package com.example.quizbackend.questions.exams.answertable;

import lombok.Data;

@Data
public class QuestionWrittenDTO {

    private Long    questionId;
    private String questionText;
    private Long answerSheetId;
    private String answerText;
    private Double qPoint;
    private  boolean response;

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    private Long questionPaperID;

}
