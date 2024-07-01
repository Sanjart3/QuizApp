package com.example.quizbackend.questions.exams.qpaper;

import com.example.quizbackend.courses.Courses;
import com.example.quizbackend.general.UserInfo;
import com.example.quizbackend.questions.RandomQuestionDTO;
import com.example.quizbackend.questions.exams.ExamType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.List;

@Data
public class ExamPapersDTO {
    private Long id;
    //private Date questionPCD; //Question Paper Created Date
    @Enumerated(EnumType.STRING)
    private ExamType examType;  //Midterm final, random
    //private Boolean status; //Visible or not For example if it is middterm exam
    // it should be visible during specific time
    //private Boolean taken; //If this question paper is taken then we should not be able to delete of questions.
    private String ExamPaperUniqueGeneratedID; //We need to generate exam paper ID so it should belong only one
    //and we need string ID not number It will appear on top of exam paper
    private String CourseCode; //Course code can be taken from course info Even Course Info will change it will stay in
    //questionPaper as it is
    private String examTaker;//Exam taker can be different comparing to exam owner Exam taker person who is solving question
    private String UniqueExamName;

    private String createdPerson; //Who created this paper It can be professor or student itself
    private Long totalPoint;
    private double gainedPoint;
    private Date started;
    private Date ended;

    private List<RandomQuestionDTO> questionsList; // serviseda qoyaman

}