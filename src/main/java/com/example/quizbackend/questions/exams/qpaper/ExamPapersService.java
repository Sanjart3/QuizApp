
package com.example.quizbackend.questions.exams.qpaper;

import com.example.quizbackend.general.Student.UserRepository;
import com.example.quizbackend.courses.CourseRepository;
import com.example.quizbackend.questions.*;
import com.example.quizbackend.questions.exams.ExamType;
import com.example.quizbackend.questions.exams.answertable.ExamAnswerSheet;
import com.example.quizbackend.questions.exams.answertable.ExamAnswerSheetDTO;
import com.example.quizbackend.questions.exams.answertable.ExamAnswerSheetRepository;
import com.example.quizbackend.questions.exams.questionlist.QuestionList;
import com.example.quizbackend.questions.exams.questionlist.QuestionListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExamPapersService {

    @Autowired
    private ExamPapersRepository examPapersRepository;
    @Autowired
    private QuestionsBankRepository questionsRepository;
    @Autowired
    AnswersRepository answersRepository;
    @Autowired
    ExamAnswerSheetRepository examAnswerSheetRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    QuestionListRepository questionListRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    //selectedQIDL  selected question ID list From front page we need to receive only ID of users.

//    public List<QuestionsBank> getQuestions(Long courseID, Long qNumber) {
//        List<QuestionsBank> questionsList = questionsRepository.findAllQuestions(courseID, qNumber);
//        return questionsList;
//
//    }

    public List<Answers> shuffleAnswers(List<Answers> object) {
        Collections.shuffle(object);
        return object;
    }

    public List<QuestionList> shuffleQuestions(List<QuestionList> object) {
        Collections.shuffle(object);
        return object;
    }

    public List<String> getStudentsByCourseId(Long id) {
        return userRepository.getStudentUsernames(id);
    }

    //query course and role userinfo mistakes should be fixed

    public String addNewQuestionPaperList(String username, Long CourseID, List<Long> selectedQIDL, ExamPapers examPapers) {
       List<ExamPapers> examPapersList =examPapersRepository.findByCoursesAndAndExamType(CourseID, String.valueOf(examPapers.getExamType()));
     if(examPapersList!=null) {
         for (ExamPapers examPaper : examPapersList
         ) {
               Date start =examPaper.getStarted();
               Date endD =examPaper.getEnded();
               if((start.after(examPaper.getStarted())&&start.before(examPaper.getEnded()))|| (endD.after(examPaper.getStarted())|| endD.before(examPapers.getEnded()))){
                   return "you cannot post this kind of paper for the time you are setting";
               }
         }
     }
        examPapers.setUserInfo(userRepository.findByUsername(username).get());
        examPapers.setQuestionPCD(new Date());
        examPapers.setExamPaperUniqueGeneratedID(UUID.randomUUID().toString());
        examPapers.setCourses(courseRepository.getOne(CourseID));
        examPapersRepository.save(examPapers);
        makeQuestionLists( CourseID, selectedQIDL, examPapers);
           return "Paper was created successfully";
    }
// BIR KIWI IKTA BR HIL PAPERNI POST QILIWINI  OLDINI OLIW KK
    public void changePaperStatus(Long courseId, String examType) {
        List<ExamPapers> examPapers = examPapersRepository.findPapers(courseId, examType);
        for (ExamPapers exampaper : examPapers
        ) {
            if (exampaper.getStarted().before(new Date()) && exampaper.getEnded().after(new Date())) {
                exampaper.setStatus(true);
                examPapersRepository.save(exampaper);
            }

        }
    }
    public ExamPapersDTO getForReview(String uniqueName,Long id){
        ExamPapersDTO examPapersDTO =new ExamPapersDTO();
        ExamPapers examPapers =examPapersRepository.findExamPapersByUniqueExamNameAndAndId(uniqueName,id);
        if(examPapers!=null) {
            examPapersDTO = modelMapper.map(examPapers, ExamPapersDTO.class);
            List<QuestionList> questionLists = questionListRepository.findAllByExamPapers(examPapers);
            List<RandomQuestionDTO> randomQuestionDTOS = new ArrayList<>();
            for (QuestionList question : questionLists
            ) {
                List<ExamAnswerSheet> examAnswerSheetList = examAnswerSheetRepository.findAllByQuestionList(question);
                List<ExamAnswerSheetDTO> examAnswerSheetDTOS = new ArrayList<>();
                for (ExamAnswerSheet examAnswer : examAnswerSheetList
                ) {
                    examAnswerSheetDTOS.add(modelMapper.map(examAnswer, ExamAnswerSheetDTO.class));
                }

                RandomQuestionDTO randomQuestionDTO = modelMapper.map(question, RandomQuestionDTO.class);
                randomQuestionDTO.setExamAnswerSheetDTO(examAnswerSheetDTOS);
                randomQuestionDTOS.add(randomQuestionDTO);
            }

            examPapersDTO.setQuestionsList(randomQuestionDTOS);
        }
            return examPapersDTO;



    }

    public ExamPapersDTO getExamPaper(Long courseId, String examType, String username) {
        if(ExamType.valueOf(examType)!=ExamType.RANDOM){
            System.out.println(11111111111111l);
        changePaperStatus(courseId,examType);}
        else{
            makeRandomPaper(courseId,examType,username);
            System.out.println(222222222222l);
        }
        ExamPapersDTO examPapersDTO = new ExamPapersDTO();
           ExamPapers ePaper =examPapersRepository.findByExamTaker(courseId,examType,username);
        if(ePaper!=null){
            examPapersDTO = modelMapper.map(ePaper, ExamPapersDTO.class);
            List<QuestionList> questionLists =questionListRepository.findAllByExamPapers(ePaper);
            List<RandomQuestionDTO> randomQuestionDTOS =new ArrayList<>();
            for (QuestionList question:questionLists
                 ) {
                List<ExamAnswerSheet> examAnswerSheetList =examAnswerSheetRepository.findAllByQuestionList(question);
                List<ExamAnswerSheetDTO> examAnswerSheetDTOS= new ArrayList<>();
                for (ExamAnswerSheet examAnswer:examAnswerSheetList
                     ) {
                    examAnswerSheetDTOS.add(modelMapper.map(examAnswer,ExamAnswerSheetDTO.class));
                }

                RandomQuestionDTO randomQuestionDTO=modelMapper.map(question,RandomQuestionDTO.class);
                randomQuestionDTO.setExamAnswerSheetDTO(examAnswerSheetDTOS);
                randomQuestionDTOS.add(randomQuestionDTO);
            }

             examPapersDTO.setQuestionsList(randomQuestionDTOS);

            return examPapersDTO;
        }
        ExamPapers examPapers = examPapersRepository.findByCoursAndAndExamTypeAndStatus(courseId, examType);
        if(courseId!=null){
            examPapers.setCourses(courseRepository.getOne(courseId));

        }
        List<RandomQuestionDTO> questionsDTOList = new ArrayList<>();
        if (examPapers != null) {
            List<QuestionList> questionsLists = questionListRepository.findAllByExamPapers(examPapers);
            shuffleQuestions(questionsLists);
            List<QuestionList> questionList = new ArrayList<>();
            Long totalPoint =0l;

            for (QuestionList questionsLis : questionsLists) {
                QuestionList questionList1 = new QuestionList();
                questionList1.setChoosen_question_id(questionsLis.getChoosen_question_id());
                questionList1.setQuestionText(questionsLis.getQuestionText());
                questionList1.setPhoto(questionsLis.getPhoto());
                questionList1.setQuestionPoint(questionsLis.getQuestionPoint());
                questionList1.setQType(questionsLis.getQType());
                questionList.add(questionList1);
                totalPoint= (long) (totalPoint+questionsLis.getQuestionPoint());
            }
            ExamPapers papers = new ExamPapers();
            papers.setQuestionPCD(new Date());
            papers.setCourses(examPapers.getCourses());
            papers.setUniqueExamName(examPapers.getUniqueExamName());
            papers.setStarted(examPapers.getStarted());
            papers.setEnded(examPapers.getEnded());
            papers.setCourseCode(examPapers.getCourseCode());
            papers.setExamPaperUniqueGeneratedID(UUID.randomUUID().toString());
            papers.setExamType(examPapers.getExamType());
            papers.setStatus(examPapers.getStatus());
            papers.setUserInfo(examPapers.getUserInfo());
            papers.setExamTaker(username);
            papers.setTotalPoint(totalPoint);
            examPapersRepository.save(papers);
            int orderQuestion = 1;
            for (QuestionList question : questionList) {
                question.setExamPapers(papers);
                question.setQuestionOrderNumber(orderQuestion++);
                questionListRepository.save(question);
                RandomQuestionDTO randomQuestionsDTO = (modelMapper.map(question, RandomQuestionDTO.class));
                //  questionLists.add(question);
                List<Answers> answersL = answersRepository.findAllByQuestionId(question.getChoosen_question_id());
                shuffleAnswers(answersL);
                List<ExamAnswerSheetDTO> examAnswerSheetDTOS =new ArrayList<>();
                for (Answers ans : answersL) {
                    ExamAnswerSheet examAnswerSheet = new ExamAnswerSheet();
                    examAnswerSheet.setQuestionList(question);
                    examAnswerSheet.setResponse(false);
                    examAnswerSheet.setAnswerText(ans.getAnswer());
                    examAnswerSheet.setCorrectA(ans.getCorrectA());
                    examAnswerSheetRepository.save(examAnswerSheet);
                    examAnswerSheetDTOS.add(modelMapper.map(examAnswerSheet,ExamAnswerSheetDTO.class));
                }
                randomQuestionsDTO.setExamAnswerSheetDTO(examAnswerSheetDTOS);
                questionsDTOList.add(randomQuestionsDTO);
            }
            examPapersDTO = modelMapper.map(papers, ExamPapersDTO.class);
            examPapersDTO.setCreatedPerson(papers.getUserInfo().getUsername());
            examPapersDTO.setQuestionsList(questionsDTOList);

        }
        return examPapersDTO;
    }

     public  void  makeRandomPaper(Long courseId, String examType, String username){
        List<QuestionsBank> questionsBanks =questionsRepository.findAllRandomQuestions(courseId,20l);
        ExamPapers examPapers=new ExamPapers();
        examPapers.setExamType(ExamType.valueOf(examType));
        examPapers.setUserInfo(userRepository.findUserInfoByUsername(username));
        examPapers.setExamTaker(username);
        examPapers.setStatus(true);
        examPapers.setQuestionPCD(new Date());
        Double totalPoint =0.0;
        List<Long> list =new ArrayList<>();
         for (QuestionsBank question:questionsBanks
              ) {
             totalPoint+=question.getPoint();
             list.add(question.getQuestion_id());


         }
         examPapers.setTotalPoint(totalPoint);
         System.out.println(333333333333333l);
         addNewQuestionPaperList(username,courseId,list,examPapers);

     }
    public  void  makeQuestionLists( Long CourseID, List<Long> selectedQIDL, ExamPapers examPapers){
        for (int i =0; i < selectedQIDL.size(); i++) {
            QuestionsBank questionsBank = questionsRepository.getOne(selectedQIDL.get(i));

            if (questionsBank.getCourseID().getCourse_id() == CourseID) {
                QuestionList questionList = new QuestionList();
                questionList.setQuestionPoint(questionsBank.getPoint());
                questionList.setQType(questionsBank.getQType());
                questionList.setPhoto(questionsBank.getPhoto());
                questionList.setQuestionText(questionsBank.getQuestion());
                questionList.setChoosen_question_id(selectedQIDL.get(i));
                questionList.setExamPapers(examPapers);
                questionListRepository.save(questionList);
            }
        }
    }
    public void updateExamPaper(String username, Long CourseID, List<Long> selectedQIDL, ExamPapers examPapers) {
        Long id = examPapers.getId();

        ExamPapers papers = examPapersRepository.findById(id).get();
        // if (papers.getStarted().before(new Date())) {
        if (papers != null) {
            if (examPapers.getStatus() != null) {
                papers.setStatus(examPapers.getStatus());
            }
            if (examPapers.getStarted() != null) {
                papers.setStarted(examPapers.getStarted());
            }
            if (examPapers.getExamType() != null) {
                papers.setExamType(examPapers.getExamType());
            }
            if (examPapers.getExamPaperUniqueGeneratedID() != null) {
                papers.setExamPaperUniqueGeneratedID(examPapers.getExamPaperUniqueGeneratedID());
            }
            if (examPapers.getEnded()!= null) {
                papers.setEnded(examPapers.getEnded());
            }
            if (examPapers.getCourseCode() != null) {
                papers.setCourseCode(examPapers.getCourseCode());
            }
            examPapersRepository.save(papers);
            if (selectedQIDL.size()!= 0) {
                List<QuestionList> questionLists = questionListRepository.findAllByExamPapers(papers);
                for (QuestionList questionList : questionLists
                ) {
                    questionList.setExamPapers(null);
                    questionListRepository.delete(questionList);
                }
                makeQuestionLists( CourseID, selectedQIDL, papers);

            }

        }


        // }
    }

    public  void  deleteExamPapers(Long id){

        ExamPapers examPapers = examPapersRepository.findById(id).get();
        //    if (papers.getStarted().before(new Date())) {
//            ExamPapers examPapers =examPapersRepository.findById(id).get();
        List<QuestionList> questionLists =questionListRepository.findAllByExamPapers(examPapers);
        for (QuestionList question:questionLists
        ) {
            question.setExamPapers(null);
            questionListRepository.delete(question);
        }
        examPapers.setUserInfo(null);
        examPapersRepository.delete(examPapers);
        // }
    }



}
    //query course and role userinfo mistakes should be fixed
//    public void makeExamPaper(String username, Long CourseID, List<Long> selectedQIDL, ExamPapers examPapers) {
//           List<QuestionsBank> tempQL = new ArrayList<>();
//           List<QuestionList> questionLists =new ArrayList<>();
//           Map<Long,List<Answers>> answerList =new HashMap<>();
//           if (courseRepository.existsById(CourseID)&&(examPapers.getExamType() == ExamType.ASSIGNMENT||examPapers.getExamType()== ExamType.MIDTERM||examPapers.getExamType()== ExamType.FINAL)) {
//                    for (int i = 0; i < selectedQIDL.size(); i++) {
//                        QuestionsBank questionsBank = questionsRepository.getOne(selectedQIDL.get(i));
//                        if (questionsBank.getCourseID().getCourse_id() == CourseID) {
//                            tempQL.add(questionsBank);
//                            QuestionList questionList =new QuestionList();
//                            questionList.setQuestionPoint(questionsBank.getPoint());
//                            questionList.setQuestionText(questionsBank.getQuestion());
//                            List<Answers> answers =answersRepository.findAllByQuestionId(questionsBank.getQuestion_id());
//                            questionList.setChoosen_question_id(selectedQIDL.get(i));
//                            questionLists.add(questionList);
//                            answerList.put(selectedQIDL.get(i),answers);
//                        }
//                    }
//
//                List<String> examTakers =getStudentsByCourseId(CourseID);
//                for (String examTaker:examTakers
//                     ) {
//                     ExamPapers papers =new ExamPapers();
//
//                if (tempQL.size() != 0) {
//
//                    papers.setQuestionPCD(new Date());
//                    papers.setExamType(examPapers.getExamType());
//                    papers.setStatus(examPapers.getStatus());
//                    papers.setTaken(false);
//                    if (examPapers.getExamPaperUniqueGeneratedID()==null) {
//                        papers.setExamPaperUniqueGeneratedID(UUID.randomUUID().toString());
//                    }
//                    papers.setCourses(courseRepository.getOne(CourseID));
//                    papers.setUserInfo(userRepository.findByUsername(username).get());
//                    papers.setExamTaker(examTaker);
//                 //   examPapers.setQuestionsList(tempQL);
//                    papers.setCourseCode(examPapers.getCourseCode());
//                    examPapersRepository.save(papers);
//                    shuffleQuestions(questionLists);
//                    int orderQuestion =1;
//                    for (QuestionList questionList:questionLists
//                    ) {
//                        questionList.setQuestionOrderNumber(orderQuestion++);
//                        questionList.setExamPapers(papers);
//                        questionListRepository.save(questionList);
//                        List<Answers> answersL =answerList.get(questionList.getChoosen_question_id());
//                        shuffleAnswers(answersL);
//                        for (Answers ans:answersL
//                             ) {
//
//                            ExamAnswerSheet examAnswerSheet =new ExamAnswerSheet();
//                            examAnswerSheet.setQuestionList(questionList);
//                            examAnswerSheet.setAnswerText(ans.getAnswer());
//                            examAnswerSheet.setCorrectA(ans.getCorrectA());
//                            examAnswerSheetRepository.save(examAnswerSheet);
//                        }
//                    }
//                }
//                }
//            }
//
//
//    }




