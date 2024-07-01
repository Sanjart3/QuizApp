package com.example.quizbackend.questions;

import com.example.quizbackend.courses.CourseRepository;
import com.example.quizbackend.courses.syllabus.SyllabusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionBankService {
    @Autowired
    private QuestionsBankRepository questionsRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private Answers answers;

    @Autowired
    private AnswersRepository answersRepository;

    public String addNewQuestion(Long courseID, QuestionsBank question)
    {
     //   List<Answers> answersList=new ArrayList<>();
        if (courseRepository.existsById(courseID)) {
            question.setCourseID(courseRepository.findById(courseID).get());
            if (question.getWeek_order()==null){
                question.setWeek_order(1);
            }
            question.setQType(question.getQType());
            question.setCreated(new Date());

            if (question.getPoint()==null) {
                question.setPoint(1.0);
            }

//            for (Answers answers : question.getAnswerList()) {
//                answersRepository.save(answers);
//                answersList.add(answers);
//            }
           // answersList=question.getAnswerList();
            //question.setAnswerList(answersList);
            //   answersRepository.saveAll(answersList);
            questionsRepository.save(question);



            return "Question added successfully to database";
        }else {
            return "Can not find related course";
        }
    }


    public String deleteQuestion(Long questionID){
        if (questionsRepository.existsById(questionID)){
            questionsRepository.deleteById(questionID);
            return "Question deleted";
        }
        else return "Can not find question";
    }

    public void updateQuestion(QuestionsBank questionsBank){
        QuestionsBank existingQuestion=questionsRepository.findByQuestion_id(questionsBank.getQuestion_id());
        Long id =existingQuestion.getQuestion_id();
        if (questionsBank.getQuestion()!=null){
            existingQuestion.setQuestion(questionsBank.getQuestion());
        }
        if (questionsBank.getStatus()!=null){
            existingQuestion.setStatus(questionsBank.getStatus());
        }
        if(questionsBank.getPhoto()!=null){
            existingQuestion.setPhoto(questionsBank.getPhoto());
        }
        if (questionsBank.getOwner()!=null){
            existingQuestion.setOwner(questionsBank.getOwner());
        }
        if (questionsBank.getPoint()!=null) {
            existingQuestion.setPoint(questionsBank.getPoint());
        }
        if (questionsBank.getQType()!=null){
            existingQuestion.setQType(questionsBank.getQType());
        }
        if (questionsBank.getUsedStatus()!=null){
            existingQuestion.setUsedStatus(questionsBank.getUsedStatus());
        }
        if (questionsBank.getWeek_order()!=null){
            existingQuestion.setWeek_order(questionsBank.getWeek_order());
        }
        System.out.println(questionsBank.getAnswerList());
        if (questionsBank.getAnswerList()!=null){

            List<Answers> answersList=answersRepository.findAllByQuestionId(id);
            for (Answers ans:answersList
            ) {
                ans.setQuestionsBank(null);
                answersRepository.delete(ans);


            }
            List<Answers> answersList1 =questionsBank.getAnswerList();
            for (Answers answers:answersList1
            ) {
                answers.setQuestionsBank(questionsBank);

            }
        }

        answersRepository.saveAll(questionsBank.getAnswerList());
        questionsRepository.save(existingQuestion);
    }


    public  List<QuestionsBank> getQuestionBanksByCourseId(Long id){
        List<QuestionsBank> questionsBanks=questionsRepository.findAllQuestionByCourseID(id);
        return questionsBanks;
    }


    public List<String> AnswerOptions(Long id){
        List<String> list =new ArrayList<>();
        List<Answers> answersList =answersRepository.findAllByQuestionId(id);
        for (Answers answers:answersList
        ) {
            list.add(answers.getAnswer());

        }
        return list;
    }
    public List<QuestionsBank> getQuestionPaper(Long courseId,String  type,Long numberOfQuestions){
        List<QuestionsBank> questionsBanksPaperList =questionsRepository.findAllQuestionsForPaper(courseId,type,numberOfQuestions);

        return questionsBanksPaperList;
    }

}
