package com.example.quizbackend.courses;

import com.example.quizbackend.general.UserInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
//TODO need to review course DTO I might forgot some parameters.
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CourseDTO {
    private Long course_id;
    private String code;
    private String name; //Name of the course It is not username
    @Column(length = 1000)
    private String info;
    @Enumerated(EnumType.STRING)
    private CourseTypes courseTypes;
    private String username;
    private Boolean courseStatus;

    public CourseDTO(Long course_id, String name, String code) {
        this.course_id = course_id;
        this.name = name;
        this.code = code;
    }

    public CourseDTO() {
    }
    //TODO list of the syllabus belong to this course
}
