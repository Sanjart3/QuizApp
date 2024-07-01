package com.example.quizbackend.questions;

import com.example.quizbackend.general.ERole;
import com.example.quizbackend.courses.Courses;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;
@Transactional
@Embeddable
@Entity
@Table(name = "questions")
@Data
public class QuestionsBank {
    @Id
    @SequenceGenerator(name = "qGenerator", sequenceName = "QUESTION_GENERATOR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qGenerator")
    @Column(name = "question_id")
    private Long question_id;
    private Integer week_order;
    @ManyToOne
    @JoinColumn(name = "question_course")
    private Courses courseID;
    @Column(length = 1000)
    private String question;
    @Enumerated(EnumType.STRING)
    private QType qType;
    private Double point;
    private Boolean status;  //Question status is possible make it public to user or only available duirng exam
    @Enumerated(EnumType.ORDINAL)
    private ERole owner;
    private Date created;



    @Lob
    private String photo;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "questionsBank")
    private List<Answers> answerList;
    private Boolean usedStatus;

}

