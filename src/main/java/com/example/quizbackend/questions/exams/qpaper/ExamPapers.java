package com.example.quizbackend.questions.exams.qpaper;

import com.example.quizbackend.general.UserInfo;
import com.example.quizbackend.courses.Courses;
import com.example.quizbackend.questions.QuestionsBank;
import com.example.quizbackend.questions.exams.ExamType;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@Transactional
@Embeddable
public class ExamPapers implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Date questionPCD; //Question Paper Created Date
    private Date started;
    private Date ended;
    private String uniqueExamName;
    @Enumerated(EnumType.STRING)
    private ExamType examType;  //Midterm final, random
    private Boolean status; //Visible or not For example if it is middterm exam
    // it should be visible during specific time
    private Boolean taken; //If this question paper is taken then we should not be able to delete of questions.
    private String ExamPaperUniqueGeneratedID; //We need to generate exam paper ID so it should belong only one
    //and we need string ID not number It will appear on top of exam paper
    private String CourseCode; //Course code can be taken from course info Even Course Info will change it will stay in
    //questionPaper as it is

    private String examTaker;//Exam taker can be different comparing to exam owner Exam taker person who is solving question

    private double totalPoint;
    private double gainedPoint=0l;
    @ManyToOne
    private Courses courses;
    @OneToOne
    private UserInfo userInfo; //Who created this paper It can be professor or student itself

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getQuestionPCD() {
        return questionPCD;
    }

    public void setQuestionPCD(Date questionPCD) {
        this.questionPCD = questionPCD;
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getTaken() {
        return taken;
    }

    public void setTaken(Boolean taken) {
        this.taken = taken;
    }

    public String getExamPaperUniqueGeneratedID() {
        return ExamPaperUniqueGeneratedID;
    }

    public void setExamPaperUniqueGeneratedID(String examPaperUniqueGeneratedID) {
        ExamPaperUniqueGeneratedID = examPaperUniqueGeneratedID;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }

    public String getExamTaker() {
        return examTaker;
    }

    public void setExamTaker(String examTaker) {
        this.examTaker = examTaker;
    }

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
