package com.example.quizbackend.professor;

import com.example.quizbackend.usersecurity.payload.response.MessageResponse;
import com.example.quizbackend.usersecurity.IdentityConfirmation;
import com.example.quizbackend.general.GeneralInfoUser;
import com.example.quizbackend.general.UserInfo;
import com.example.quizbackend.general.Student.UserRepository;
import com.example.quizbackend.usersecurity.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@CrossOrigin
@RequestMapping("/api/")
public class ProfessorController {
  private final UserRepository professorRepository;
  @Autowired
  ProfessorService professorService;
  @Autowired
  IdentityConfirmation identity;
  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  public ProfessorController(UserRepository professorRepository) {
    this.professorRepository = professorRepository;
  }
  //This is a mian API for professor interface to get General Data, It will store all data to GeneralInfoUser interface
  //TODO currently professor ID coming  coming from PathVariable Need to move inside JSON body
  //TODO need to get all course belong to one professor
  @GetMapping( "/professor/{professorID}" )
  @PreAuthorize("hasRole('ROLE_PROFESSOR')")
  public ResponseEntity<?> getInfo(@PathVariable("professorID") String username, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue) {
    GeneralInfoUser generalInfoUser = null;
    if (identity.confirmUserIDwithToken(jwtValue,username)) {
      return ResponseEntity.ok(professorService.getGeneralInfo(username));
    }
    return ResponseEntity.badRequest().body(new MessageResponse("Can't find professor with username "+username));
  }

  //TODO currently username coming from PathVariable Need to move it to JSON body

  @PutMapping("/professor/{username}")
  @PreAuthorize("hasRole('ROLE_PROFESSOR')")
  public ResponseEntity<?> update(@PathVariable("username") String username,
                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue,
                                  @RequestBody UserInfo professorInfo){
    if (professorService.updateProfessor(username, professorInfo)){
    return ResponseEntity.ok().body(new MessageResponse("USER INFORMATION UPDATED SUCCESSFULLY!"));
    }
    return ResponseEntity.badRequest().body(new MessageResponse("USER NOT FOUND FROM DATABASE!"));
  }

}

