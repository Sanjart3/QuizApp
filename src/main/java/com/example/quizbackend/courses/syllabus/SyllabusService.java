package com.example.quizbackend.courses.syllabus;

import com.example.quizbackend.courses.CourseRepository;
import com.example.quizbackend.courses.Courses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SyllabusService {
    @Autowired
    private final SyllabusRepository syllabusRepository;
    @Autowired
    private final CourseRepository courseRepository;



    public SyllabusService(SyllabusRepository syllabusRepository, CourseRepository courseRepository) {
        this.syllabusRepository = syllabusRepository;
        this.courseRepository = courseRepository;
    }
    public String addSyllabus(Long courseID,Syllabus syllabus) {
        if (courseRepository.existsById(courseID))
        {
            Courses courses=courseRepository.findById(courseID).get();
            if (syllabus.getWeek_order()==null) {
                syllabus.setWeek_order(1);
            }
            syllabus.setCourses(courses);
            syllabusRepository.save(syllabus);
            return "Syllabus saved successfully";
        }else {
           return "Can not find related course";
        }
    }
    //In this case it will not delete all syllabus it will delete only one week information
    public String deleteSyllabus(Long syllabusID) {
        if (syllabusRepository.findById( syllabusID).isPresent()){
            syllabusRepository.deleteById(syllabusID);
            return "Syllabus successfully deleted!";
        }
        else {
            return "Syllabus not found";
        }
    }
    //Professor can update some part of the course if it will pass course information
    //We can pass syllabus ID only
    public String updateSyllabus(Syllabus syllabus) {
        if (syllabusRepository.existsById(syllabus.getSyllabus_id())) {
            Syllabus existing = syllabusRepository.getOne(syllabus.getSyllabus_id());//.get();
            if (syllabus.getWeek_text() != null) {
                existing.setWeek_text(syllabus.getWeek_text());
            }
            if (existing.getWeek_order() != null) {
                existing.setWeek_order(syllabus.getWeek_order());
            }
            if (syllabus.getCourses()!=null){
                if (courseRepository.existsById(syllabus.getCourses().getCourse_id())){
                existing.setCourses(syllabus.getCourses());
                }
                else return "Course not found";

            }
            syllabusRepository.save(existing);
            return "Syllabus Updated Successfully!";
        }
        else {
            return "Syllabus not found!";
        }
    }

    public void deleteByCourseId(List<Syllabus> syllabusList ){
        syllabusRepository.deleteAll(syllabusList);
    }
    public List<Syllabus> getAllSyllabus(Long courseID){
        return syllabusRepository.getAllByCourseID(courseID);
    }
}
