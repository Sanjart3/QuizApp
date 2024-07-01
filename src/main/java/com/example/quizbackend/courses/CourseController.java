package com.example.quizbackend.courses;


import com.example.quizbackend.general.UserInfo;
import com.example.quizbackend.professor.ProfessorService;
import com.example.quizbackend.usersecurity.IdentityConfirmation;
import com.example.quizbackend.professor.ProfessorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(path = "api/course")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    ProfessorService professorService;
    @Autowired
    private IdentityConfirmation identity;
    @Autowired
    ModelMapper modelMapper;


    //Getting all course list by professor's ID
    //TODO Need to move user name to JSON body and need to work with ID only
    @GetMapping("/listcourse/userID")
    public ResponseEntity<?> userCourses(@RequestBody UserInfo userInfoDTO,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue){
        Map<String, Object> jsonResponceMap=new LinkedHashMap<String, Object>();
        List<CourseDTO> coursesList= courseService.getCoursesOfUser(userInfoDTO.getId());

        List<CourseDTO> courseDTOList=new ArrayList<CourseDTO>();
        if (identity.confirmUserIDwithToken(jwtValue, userInfoDTO.getUsername())) {
            if (!coursesList.isEmpty()){
                for (CourseDTO courses:coursesList){
                    courseDTOList.add(modelMapper.map(courses, CourseDTO.class));
                }
                jsonResponceMap.put("status",1);
                jsonResponceMap.put("data",courseDTOList);
                return new ResponseEntity<>(jsonResponceMap, HttpStatus.OK);
            }else {
                jsonResponceMap.clear();
                jsonResponceMap.put("status",0);
                jsonResponceMap.put("message","There is no courses available");
                return new ResponseEntity<>(jsonResponceMap,HttpStatus.NOT_FOUND);
            }
        } else {
            jsonResponceMap.clear();
            jsonResponceMap.put("status",0);
            jsonResponceMap.put("message","Cannot confirm username");
            return new ResponseEntity<>(jsonResponceMap,HttpStatus.METHOD_NOT_ALLOWED);
        }

    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    //TODO Need to move user name to JSON body
    @PostMapping(value = "/add/{userId}")
    public ResponseEntity<?>  addCourse(@Validated @PathVariable("userId") Long professorID,
                                        @RequestBody CourseDTO courseDTO,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue ) {
        Map<String, Object> jsonResponseMap = new LinkedHashMap<String, Object>();
        if (courseService.isProfessorWithId(professorID)) {
            //convert DTO to an entity
            Courses courses = modelMapper.map(courseDTO, Courses.class);
            courseService.addNewCourse(professorService.getProfessorUserNameById(professorID), courses);
            jsonResponseMap.put("status", 1);
            jsonResponseMap.put("message", "Course is Added Successfully!");
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.CREATED);
        }
        else {
            jsonResponseMap.clear();
            jsonResponseMap.put("status",0);
            jsonResponseMap.put("message","Cannot find professor from database");
            return new ResponseEntity<>(jsonResponseMap,HttpStatus.NOT_FOUND);
        }
    }





    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    //TODO Need to move user name to JSON body
    @PutMapping(value = "update/{userId}")
    public ResponseEntity<?> updateCourse(@Validated @RequestBody CourseDTO courseDTO,
                                          @PathVariable Long userId,
                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue){
        Map<String, Object> jsonResponseMap = new LinkedHashMap<String, Object>();
        if (identity.confirmUserIDwithToken(jwtValue, courseDTO.getUsername())) {
            try {
                courseService.updateCourse(courseDTO);
                jsonResponseMap.put("status", 1);
                jsonResponseMap.put("data", "Course updated Successfully!");

                return new ResponseEntity < > (jsonResponseMap, HttpStatus.OK);
            } catch (Exception ex) {
                jsonResponseMap.clear();
                jsonResponseMap.put("status", 0);
                jsonResponseMap.put("message", "Data is not found");
                return new ResponseEntity < > (jsonResponseMap, HttpStatus.NOT_FOUND);
            }
        }
        else{
            jsonResponseMap.clear();
            jsonResponseMap.put("status",0);
            jsonResponseMap.put("message","Cannot confirm username");
            return new ResponseEntity<>(jsonResponseMap,HttpStatus.METHOD_NOT_ALLOWED);
        }
    }
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    //TODO Need to move user name to JSON body
    @DeleteMapping(value = "delete/{userId}")
    public ResponseEntity<?> deleteCourse(@Validated @PathVariable("userId") Long userId,
                                          @RequestBody CourseDTO courseDTO,
                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue){
        Map<String, Object> jsonResponseMap = new LinkedHashMap<String, Object>();
        if (identity.confirmUserIDwithToken(jwtValue, professorService.getUsernameById(userId))) {
            try {
                System.out.println("AFTER DELETE");
                courseService.deleteCourse(courseDTO.getCourse_id(), professorService.getUsernameById(userId));
                System.out.println("BEFORE DELETE");
                jsonResponseMap.put("status", 1);
                jsonResponseMap.put("data", "Course deleted Successfully!");
                return new ResponseEntity < > (jsonResponseMap, HttpStatus.OK);
            } catch (Exception ex) {
                jsonResponseMap.clear();
                jsonResponseMap.put("status", 0);
                jsonResponseMap.put("message", "Course is not  found");
                return new ResponseEntity < > (jsonResponseMap, HttpStatus.NOT_FOUND);
            }
        }
        else{
            jsonResponseMap.clear();
            jsonResponseMap.put("status",0);
            jsonResponseMap.put("message","Cannot confirm username");
            return new ResponseEntity<>(jsonResponseMap,HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @GetMapping("/listcourse")
    public ResponseEntity getAllCoursesAvailable(){
        Map<String, Object> jsonResponseMap = new LinkedHashMap<>();
        List<CourseDTO> courseList =  courseService.getAllCourses();
        List<CourseDTO> courseDTOList=new ArrayList<CourseDTO>();
        if (!courseList.isEmpty()){
            for (CourseDTO course:courseList){
                courseDTOList.add(modelMapper.map(course, CourseDTO.class));
            }
            jsonResponseMap.put("status",1);
            jsonResponseMap.put("data",courseDTOList);
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.OK);
        } else {
            jsonResponseMap.clear();
            jsonResponseMap.put("status",0);
            jsonResponseMap.put("message","There is no courses available");
            return new ResponseEntity<>(jsonResponseMap,HttpStatus.NOT_FOUND);
        }
    }
}
