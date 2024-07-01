
package com.example.quizbackend.questions.exams.answertable;

import com.example.quizbackend.questions.QType;
import com.example.quizbackend.questions.RandomQuestionDTO;
import com.example.quizbackend.questions.exams.qpaper.ExamPapers;
import com.example.quizbackend.questions.exams.qpaper.ExamPapersDTO;
import com.example.quizbackend.questions.exams.qpaper.ExamPapersRepository;
import com.example.quizbackend.questions.exams.questionlist.QuestionList;
import com.example.quizbackend.questions.exams.questionlist.QuestionListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamAnswerSheetService {
//    @Autowired
        private final QuestionListRepository questionListRepository;
    @Autowired
    ExamPapersRepository examPapersRepository;
private  final ExamAnswerSheetRepository examAnswerSheetRepository;

    public ExamAnswerSheetService(QuestionListRepository questionListRepository, ExamAnswerSheetRepository examAnswerSheetRepository) {
        this.questionListRepository = questionListRepository;
        this.examAnswerSheetRepository = examAnswerSheetRepository;
    }


    public Double setResponse(ExamPapersDTO examPapersDTO) {
    ExamAnswerSheetService examAnswerSheetService =new ExamAnswerSheetService(questionListRepository, examAnswerSheetRepository);
    List<RandomQuestionDTO> randomQuestionDTOList =examPapersDTO.getQuestionsList();

    Double points =examAnswerSheetService.putResponses(randomQuestionDTOList);
    ExamPapers examPapers =examPapersRepository.findById(examPapersDTO.getId()).get();
    examPapers.setGainedPoint(points);
    examPapersRepository.save(examPapers);
    return  points;


}

public Double putResponses(List<RandomQuestionDTO> randomQuestionDTOList){
    double points =0l;
    for (RandomQuestionDTO randomQuestion:randomQuestionDTOList
         ) {
        if(randomQuestion.getQType()!= QType.WRITTEN){
            List<ExamAnswerSheetDTO> examAnswerSheetDTOS =randomQuestion.getExamAnswerSheetDTO();
            for (ExamAnswerSheetDTO examAnswerSheet:examAnswerSheetDTOS
                 ) {
                ExamAnswerSheet sheet =examAnswerSheetRepository.findByID(examAnswerSheet.getExam_answer_sheet_id());
                sheet.setResponse(examAnswerSheet.getResponse());
                if(sheet.getCorrectA()==true&&examAnswerSheet.getResponse()==true){
                    points =points+randomQuestion.getQuestionPoint();
                }
                examAnswerSheetRepository.save(sheet);

            }
        }else{
            if(randomQuestion.getQuestion()!=null&&randomQuestion.getExamAnswerSheetDTO().size()!=0) {
                ExamAnswerSheet examAnswerSheet = new ExamAnswerSheet();
                ExamAnswerSheet sheet = examAnswerSheetRepository.findByQuestionId(randomQuestion.getId());
                if (sheet != null) {
                    sheet.setAnswerText(randomQuestion.getExamAnswerSheetDTO().get(0).getAnswerText());
                } else {

                    examAnswerSheet.setAnswerText(randomQuestion.getExamAnswerSheetDTO().get(0).getAnswerText());
                    examAnswerSheet.setQuestionList(questionListRepository.findById(randomQuestion.getId()).get());
                    examAnswerSheetRepository.save(examAnswerSheet);
                }
            }

        }
    }
    return points;

}
 public List<QuestionWrittenDTO> getWritten(String uniqueName){
        List<ExamPapers> examPapersList =examPapersRepository.findPapersToCheck(uniqueName);

        List<QuestionWrittenDTO> questionWrittenDTOS =new ArrayList<>();
     for (ExamPapers paper:examPapersList
          ) {
         Long id =paper.getId();
         List<QuestionList> list =questionListRepository.findAll(id);
         for (QuestionList question:list
              ) {
             QuestionWrittenDTO questionWrittenDTO =new QuestionWrittenDTO();
             Long qID =question.getId();
             ExamAnswerSheet examAnswerSheet =examAnswerSheetRepository.findWritten(qID);
             if(examAnswerSheet!=null) {
                 questionWrittenDTO.setAnswerSheetId(examAnswerSheet.getExam_answer_sheet_id());
                 questionWrittenDTO.setAnswerText(examAnswerSheet.getAnswerText());
                 questionWrittenDTO.setQuestionText(question.getQuestionText());
                 questionWrittenDTO.setQuestionId(question.getId());
                 questionWrittenDTO.setQuestionPaperID(id);
                 questionWrittenDTO.setResponse(false);
                 questionWrittenDTO.setQPoint(question.getQuestionPoint());
                 questionWrittenDTOS.add(questionWrittenDTO);
             }
         }

     }
     return questionWrittenDTOS;
 }


 public void markWritten(List<QuestionWrittenDTO> questionWrittenDTOS){
     for (QuestionWrittenDTO question:questionWrittenDTOS
          ) {
       boolean mark =  question.isResponse();
         ExamAnswerSheet examAnswerSheet = examAnswerSheetRepository.findByID(question.getAnswerSheetId());


         if(mark){
           ExamPapers examPapers =examPapersRepository.findById(question.getQuestionPaperID()).get();
           Double point =examPapers.getGainedPoint();
           if(examAnswerSheet.getResponse()==null){
           point=point+question.getQPoint();}
           examPapers.setGainedPoint(point);
           examPapersRepository.save(examPapers);
       }
         examAnswerSheet.setResponse(mark);
         examAnswerSheetRepository.save(examAnswerSheet);
     }
 }

}
