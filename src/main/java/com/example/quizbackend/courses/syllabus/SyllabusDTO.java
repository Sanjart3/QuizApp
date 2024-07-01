package com.example.quizbackend.courses.syllabus;

import com.example.quizbackend.courses.Courses;
import lombok.Data;

import javax.persistence.*;
@Data
public class SyllabusDTO {
    private Long syllabus_id;
    private Integer week_order;
    private String week_text;

}
