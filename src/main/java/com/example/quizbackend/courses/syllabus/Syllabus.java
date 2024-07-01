package com.example.quizbackend.courses.syllabus;


import com.example.quizbackend.courses.Courses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table
@Entity(name = "syllabus")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Syllabus {
    @Id
    @SequenceGenerator(name = "syllabusGenerator", sequenceName = "SYLLABUS_SEQUNCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "syllabusGenerator")
    @Column(name = "syllabus_id", nullable = false)
    private Long syllabus_id;
    private Integer week_order; //First week second week Total 16 week including Midterm/Final
    private String week_text;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses courses;
}
