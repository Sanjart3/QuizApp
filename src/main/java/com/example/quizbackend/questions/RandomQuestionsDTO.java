package com.example.quizbackend.questions;


import javax.persistence.Column;
import java.util.List;
import lombok.Data;


@Data
public class   RandomQuestionsDTO {
    private Long question_id;
    @Column(length = 1000)
    private String question;
    private List<String> answers;
}
