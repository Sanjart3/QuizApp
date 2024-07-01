package com.example.quizbackend.questions;

import com.example.quizbackend.courses.Courses;
import com.example.quizbackend.general.ERole;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Data
public class     QuestionBankDTO {
    private Long question_id;
    private Integer week_order;
    @Column(length = 1000)
    private String question;
    @Enumerated(EnumType.STRING)
    private QType qType;
   private Double point;
    private Boolean status;  //Question status is possible make it public to user or only available during exam
    @Enumerated(EnumType.ORDINAL)
   private ERole owner;
   private List<String> answers;
    private byte[] photo;
  // private  List<String> //;
    private Boolean usedStatus;
}
