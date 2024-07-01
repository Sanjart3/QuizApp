package com.example.quizbackend.questions;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Data
@Component
public class Answers {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    String answer;
    Boolean correctA;
    @ManyToOne  (fetch = FetchType.LAZY)
    @JoinColumn(name = "question_ID")
    QuestionsBank questionsBank;

}
