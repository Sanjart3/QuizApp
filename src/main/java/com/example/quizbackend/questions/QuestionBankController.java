package com.example.quizbackend.questions;

import com.example.quizbackend.courses.CourseRepository;
import com.example.quizbackend.general.UserInfo;
import com.example.quizbackend.professor.ProfessorRepository;
import com.example.quizbackend.usersecurity.IdentityConfirmation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/questions")
public class QuestionBankController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private IdentityConfirmation identity;
    @Autowired
    private QuestionsBankRepository questionsBankRepository;
    @Autowired
    AnswersRepository answersRepository;

    @Autowired
    Answers answers;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private QuestionBankService questionBankService;
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @PostMapping(value = "/add/{userID}")
    public void addQuestion(@Validated @PathVariable Long userID,
                            @RequestBody QuestionsBank questionsBank, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue ) throws IOException {


        UserInfo user =professorRepository.findById(userID).get();
        String username =user.getUsername();

        Long courseID = questionsBank.getCourseID().getCourse_id();
        if (identity.confirmUserIDwithToken(jwtValue, username)&&professorRepository.existsByUsername(username)) {

            questionBankService.addNewQuestion(courseID, questionsBank);
            List<Answers> answersList =questionsBank.getAnswerList();
            if(questionsBank.getQType()!=QType.WRITTEN) {
                for (Answers answer : answersList
                ) {

                    answer.setQuestionsBank(questionsBank);

                }

                answersRepository.saveAll(answersList);
            }

            } else throw new IllegalStateException("PROFESSOR NOT FOUND OR PROFESSOR ID DOESNT MATCH WITH TOKEN ID");


    }



    @PreAuthorize("hasRole('ROLE_PROFESSOR')")

    @PutMapping(value = "/update/{userID}")
    public ResponseEntity<?> updateQuestion(@Validated @PathVariable("userID") Long userID,
                                             @RequestBody QuestionsBank questionsBank,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue ){
        UserInfo user =professorRepository.findById(userID).get();
        String username =user.getUsername();

        if (identity.confirmUserIDwithToken(jwtValue, username)&&professorRepository.existsByUsername(username)) {
             if(questionsBankRepository.findByQuestion_id(questionsBank.getQuestion_id())==null){
                 System.out.println(2);
                 return new ResponseEntity<>("Bu iddagi savol mavjud emas", HttpStatus.BAD_REQUEST);
             }
               questionBankService.updateQuestion(questionsBank);


        } else throw new IllegalStateException("PROFESSOR NOT FOUND OR PROFESSOR ID DOESNT MATCH WITH TOKEN ID");


        return new ResponseEntity<>("status", HttpStatus.OK);
    }
   @PreAuthorize("hasRole('ROLE_PROFESSOR')")
   @DeleteMapping(value = "/delete/{userID}")
    public ResponseEntity deleteQuestion(
            @Validated @PathVariable Long userID, @RequestParam Long id,
                               @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue )  {

       UserInfo user =professorRepository.findById(userID).get();
       String username =user.getUsername();
        if (identity.confirmUserIDwithToken(jwtValue, username)&&professorRepository.existsByUsername(username)) {
          if(questionsBankRepository.findByQuestion_id(id)==null){
              return new ResponseEntity<>("bu iddagi question mavjud emas", HttpStatus.BAD_REQUEST);
          }
         List<Answers> answers1 =answersRepository.findAllByQuestionId(id);
            for (Answers answer:answers1
                 ) {
                Long id1 =answer.getId();
                answer.setQuestionsBank(null);
                answersRepository.deleteById(id1);

            }
         QuestionsBank questionsBank =questionsBankRepository.findByQuestion_id(id);
         questionsBank.setCourseID(null );
         questionBankService.deleteQuestion(id);

        }

       else{
            return  new ResponseEntity<>("token dagi user siz emassiz",HttpStatus.BAD_REQUEST);

       }
         return  ResponseEntity.ok("Savol muvaffaqiyatli o`chirildi");
    }
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR','ROLE_STUDENT')")

    @GetMapping("/getQuestions/{id}")
    public  ResponseEntity<List<QuestionsBank>> get(@PathVariable Long id){
        Boolean bool =courseRepository.existsById(id);
        if(bool==false){
            return new ResponseEntity("bu kurs id yoq", HttpStatus.BAD_REQUEST);
        }
        List<QuestionsBank> questionsBanks =questionBankService.getQuestionBanksByCourseId(id);
        if(questionsBanks.size()==0){
            return  new ResponseEntity("bu kurs idga question topilmadi",HttpStatus.OK);
        }
      List<QuestionBankDTO> questionBankDTOS =new ArrayList<>();
        for (QuestionsBank questionbank:questionsBanks
             ) {

            questionBankDTOS.add(modelMapper.map(questionbank,QuestionBankDTO.class));


        }
        for (QuestionBankDTO questionbankdto :questionBankDTOS
             ) {
            questionbankdto.setAnswers(questionBankService.AnswerOptions(questionbankdto.getQuestion_id()));
        }
       // System.out.println(questionBankDTOS);
        return  new ResponseEntity(questionBankDTOS,HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @GetMapping("/getQuestionPaper")
    public ResponseEntity<List<RandomQuestionsDTO>> getQuestionPaper(
                                           @RequestParam Long courseId,
                                           @RequestParam String type,@RequestParam Long numberOfQuestions) {

    List<RandomQuestionsDTO> questionBankDTOList = new ArrayList<>();
    List<QuestionsBank>questionsBankList = questionBankService.getQuestionPaper(courseId, type,numberOfQuestions);

    for (QuestionsBank questionsbank:questionsBankList
         ) {

        questionBankDTOList.add(modelMapper.map(questionsbank, RandomQuestionsDTO.class));
    }
    for (RandomQuestionsDTO questionBankDTO:questionBankDTOList
         ) {

        questionBankDTO.setAnswers(questionBankService.AnswerOptions(questionBankDTO.getQuestion_id()));
    }
return new ResponseEntity<>(questionBankDTOList,HttpStatus.OK);
    }


}
