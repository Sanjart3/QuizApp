package com.example.quizbackend.courses;

import com.example.quizbackend.general.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Data
@Transactional
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Courses {
    @Id
    @SequenceGenerator(name = "courseGenerator",
            sequenceName = "COURSE_SEQUNCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "courseGenerator")
    @Column(name = "course_id", nullable = false)
    private Long course_id;
    private String code;
    private Date created_date;
    private String name;
    @Column(length = 1000)
    private String info;
    private String username; //this is an owner of the course usually
    // course can create by professor only
    @Enumerated(EnumType.STRING)
    private CourseTypes courseTypes;
    private Boolean courseStatus;
    //if current status of course active or inactive.
    // We need to think if it is necessary to keep all course or not
    @ManyToMany(fetch =FetchType.LAZY)
    List<UserInfo> userInfos;

}

