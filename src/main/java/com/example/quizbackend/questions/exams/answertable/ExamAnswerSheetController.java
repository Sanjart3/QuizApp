package com.example.quizbackend.questions.exams.answertable;

import com.example.quizbackend.professor.ProfessorRepository;
import com.example.quizbackend.questions.exams.qpaper.ExamPapers;
import com.example.quizbackend.questions.exams.qpaper.ExamPapersDTO;
import com.example.quizbackend.questions.exams.qpaper.ExamPapersRepository;
import com.example.quizbackend.usersecurity.IdentityConfirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class ExamAnswerSheetController {
    @Autowired
    ExamPapersRepository examPapersRepository;
    @Autowired
     IdentityConfirmation identity;
    @Autowired
    ProfessorRepository professorRepository;
    @Autowired
    ExamAnswerSheetService examAnswerSheetService;
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")

    @PostMapping("/submit/examSheet")
    public Double submit(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue,

                       @RequestBody ExamPapersDTO examPapersDTO){
       Double points=null;
        if (identity.confirmUserIDwithToken(jwtValue, examPapersDTO.getExamTaker())) {
             points = examAnswerSheetService.setResponse(examPapersDTO);

        }
        ExamPapers examPapers =examPapersRepository.findExamPapersById(examPapersDTO.getId());
        examPapers.setStatus(false);
        examPapersRepository.save(examPapers);
        return points;

        }
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @GetMapping("/getQuestions/written/{uniqName}")
    public List<QuestionWrittenDTO> getWritten(@PathVariable String uniqName){
        return examAnswerSheetService.getWritten(uniqName);


    }
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
@PostMapping("/post/written")
    public void postWritten(@RequestBody List<QuestionWrittenDTO> questionWrittenDTOS){
      examAnswerSheetService.markWritten(questionWrittenDTOS);
    }



}
