package com.example.quizbackend.courses.syllabus;

import com.example.quizbackend.courses.CourseDTO;
import com.example.quizbackend.courses.Courses;
import com.example.quizbackend.usersecurity.IdentityConfirmation;
import com.example.quizbackend.courses.CourseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin

@RestController
@RequestMapping(path = "api/course/syllabus")
public class SyllabusController {
    @Autowired
    SyllabusRepository syllabusRepository;
    @Autowired
    SyllabusService syllabusService;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    private IdentityConfirmation identity;
    @Autowired
    ModelMapper modelMapper;
    Map<String, Object> jsonResponseMap=new LinkedHashMap<>();
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @PostMapping("add/{username}")
    public ResponseEntity<?> addNewSyllabus(@Validated@PathVariable("username") String username,
                                         @RequestBody Syllabus syllabus,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue){

        Long courseID=syllabus.getCourses().getCourse_id();
        if (identity.confirmUserIDwithToken(jwtValue, username)) {
            String message =syllabusService.addSyllabus(courseID, syllabus);
            jsonResponseMap.put("status",1);
            jsonResponseMap.put("message",   message);
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.CREATED);
        }
        else {
            jsonResponseMap.clear();
            jsonResponseMap.put("status",0);
            jsonResponseMap.put("message","User identity not confirmed ");
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.PRECONDITION_FAILED);
        }
    }
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @PostMapping("update/{username}")
    public ResponseEntity<?> updateSyllabus(@Validated @PathVariable("username") String username,
                               @RequestBody Syllabus syllabus,
                               @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue){
        if (identity.confirmUserIDwithToken(jwtValue, username)) {
            if (syllabus.getSyllabus_id()!=null) {
                String message=syllabusService.updateSyllabus(syllabus);
                jsonResponseMap.put("status",1);
                jsonResponseMap.put("message", message);
                return new ResponseEntity<>(jsonResponseMap, HttpStatus.OK);
            }
            else {
                jsonResponseMap.clear();
                jsonResponseMap.put("status",0);
                jsonResponseMap.put("message","Syllabus ID not found from request !");
                return new ResponseEntity<>(jsonResponseMap, HttpStatus.NOT_FOUND);
            }
        }
        else {
            jsonResponseMap.clear();
            jsonResponseMap.put("status",0);
            jsonResponseMap.put("message","Authentication Error");
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.NOT_FOUND);
        }

    }
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @DeleteMapping("delete/{username}")
    public ResponseEntity<?> deleteSyllabus(@Validated
                                   @PathVariable("username") String username,
                               @RequestBody Syllabus syllabus,
                               @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue){
        if (identity.confirmUserIDwithToken(jwtValue, username)) {
                String message=syllabusService.deleteSyllabus(syllabus.getSyllabus_id());
                jsonResponseMap.clear();
                jsonResponseMap.put("status",1);
            jsonResponseMap.put("message", message);
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.OK);
        }
        else {
            jsonResponseMap.clear();
            jsonResponseMap.put("status",0);
            jsonResponseMap.put("message","Authentication Error");
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("all")
    public ResponseEntity<?> getSyllabusByCourseID(@RequestBody Courses courses){

        List<Syllabus> syllabusList= syllabusService.getAllSyllabus(courses.getCourse_id());
        List<SyllabusDTO> syllabusDTOSList=new ArrayList<SyllabusDTO>();
        if (!syllabusList.isEmpty()){
            for (Syllabus syllabus:syllabusList){
                syllabusDTOSList.add(modelMapper.map(syllabus, SyllabusDTO.class));
            }
            jsonResponseMap.put("status",1);
            jsonResponseMap.put("data",syllabusDTOSList);
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.OK);
        }
        else {
            jsonResponseMap.clear();
            jsonResponseMap.put("status",0);
            jsonResponseMap.put("message","Syllabus not available");
            return new ResponseEntity<>(jsonResponseMap,HttpStatus.NOT_FOUND);
        }
    }






}
