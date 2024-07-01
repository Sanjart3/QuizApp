package com.example.quizbackend.questions.exams.qpaper;

import com.example.quizbackend.general.Student.UserRepository;
import com.example.quizbackend.general.UserInfo;
import com.example.quizbackend.questions.QuestionBankService;
import com.example.quizbackend.questions.exams.ExamType;
import com.example.quizbackend.usersecurity.IdentityConfirmation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/questionPaperList/")
public class ExamPapersController {
   @Autowired
   private ExamPapersService examPapersService;
    @Autowired
   private IdentityConfirmation identity;
    @Autowired
    ExamPapersRepository examPapersRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
   @Autowired
   private QuestionBankService questionBankService;
    @PostMapping(value = "/add/{userID}/{courseID}/{selectedQ}")
    public String addQuestionPaperList1(@PathVariable("userID") Long userID,@PathVariable("courseID") Long courseID,
                                        @PathVariable("selectedQ") List<Long> selectedQL,
                                        @RequestBody ExamPapers examPapers)
                                     //   @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue)

   {

       UserInfo userInfo = userRepository.findById(userID).get();
       String username =userInfo.getUsername();

       String message ="Your full authentication is required";

     //  if (identity.confirmUserIDwithToken(jwtValue, username)) {
          List <ExamPapers> papers =examPapersRepository.findByUniqueExamName(examPapers.getUniqueExamName());
           if(papers.size()!=0){
               message ="Please change the name of your examPapers name to be unique";
           }else {
               message= examPapersService.addNewQuestionPaperList(username, courseID, selectedQL, examPapers);

           }

      // }
return message;
   }
   @GetMapping("paper/review/{userID}/{uniqName}")
   public ResponseEntity<ExamPapersDTO> reviewPaper(@PathVariable("userID") Long userID,

                                    @PathVariable String uniqName,
                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue){


 ExamPapersDTO examPapersDTO =examPapersService.getForReview(uniqName,userID);
       Map<String, Object> jsonResponseMap = new LinkedHashMap<String, Object>();

       if(examPapersDTO.getEnded().before(new Date())){
jsonResponseMap.put("Exam did not eded", HttpStatus.BAD_REQUEST);
     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
 }
       return ResponseEntity.ok(examPapersDTO);

   }
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @GetMapping("/get/exampapers/{userID}/{courseId}/{examType}")
    public ExamPapersDTO getExamPaper(@PathVariable("userID") Long userID,
                                                      @PathVariable Long courseId,
                                                      @PathVariable String examType,
                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue) {
        UserInfo userInfo = userRepository.findById(userID).get();
        String username =userInfo.getUsername();
        System.out.println("sssssssssssssssssssss"+username   );

        ExamPapersDTO examPapersDTO =new ExamPapersDTO();
        if (identity.confirmUserIDwithToken(jwtValue, username)) {
            examPapersDTO =examPapersService.getExamPaper(courseId,examType,username);
            if(examPapersDTO.getEnded()!=null&&examPapersDTO.getEnded().after(new Date())){
                examPapersDTO.setGainedPoint(0);
            }

        }
        return examPapersDTO;

    }
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")

    @GetMapping ("/getExamPapers/bunch/{studentID}")
    public  List<ExamPapersDTO>  getPapersz(@PathVariable Long  studentID){
        UserInfo userInfo=userRepository.findById(studentID).get();
        String username =userInfo.getUsername();
        List<ExamPapers> examPapersList =examPapersRepository.findByStudent(username);
        List<ExamPapersDTO > examPapersDTOS =new ArrayList<>();
        for (ExamPapers ePaper:examPapersList
             ) {
            examPapersDTOS.add(modelMapper.map(ePaper,ExamPapersDTO.class));

        }
        return examPapersDTOS;




    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @GetMapping("/review/exampapers/{uniquePaperName}/{studentID}")
    public ExamPapersDTO getReview(
                                   @PathVariable Long studentID,
                                   @PathVariable String uniquePaperName,
                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue){
        ExamPapersDTO examPapersDTO =new ExamPapersDTO();
 String username =userRepository.findUserInfoById(studentID).getUsername();
        if(identity.confirmUserIDwithToken(jwtValue,username)){
            examPapersDTO =examPapersService.getForReview(uniquePaperName,studentID);
        }
        return examPapersDTO;
    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @PutMapping(value = "/update/{userID}/{courseId}/{selectedQ}")

    public  void updateExamPaper(@PathVariable("userID") Long userID,
                                             @PathVariable Long courseId,
                                           @PathVariable("selectedQ") List<Long> selectedQL,
                                           @RequestBody ExamPapers examPapers,

                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue){
        UserInfo userInfo = userRepository.findById(userID).get();
        String username =userInfo.getUsername();

        if (identity.confirmUserIDwithToken(jwtValue, username)) {
         examPapersService.updateExamPaper(username,courseId,selectedQL,examPapers);

        }

    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @DeleteMapping("delete/{id}")
    public  void deleteExamPaper(@PathVariable Long id){
        examPapersService.deleteExamPapers(id);
    }
    }
