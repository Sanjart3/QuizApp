package com.example.quizbackend.courses;

import com.example.quizbackend.courses.syllabus.Syllabus;
import com.example.quizbackend.courses.syllabus.SyllabusService;
import com.example.quizbackend.professor.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ProfessorRepository professorRepository;

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SyllabusService syllabusService;
    //TODO we need to check IDENTITY OF USER before adding course

    public void addNewCourse(String username /*Professor usernameonly*/, Courses course) {
        if (professorRepository.existsByUsername(username))
        {
            course.setUserInfos(List.of(professorRepository.getByUsernameAllInfo((username))));
            course.setUsername(username);
            course.setCreated_date(new Date());
            course.setCourseStatus(true);
            //Default course type is lecture
            if (course.getCourseTypes()==null){
                course.setCourseTypes(CourseTypes.LECTURE);
            }else {
                course.setCourseTypes(course.getCourseTypes());}

            courseRepository.save(course);
        }else {
            throw new IllegalStateException("Professor name can't find from database");
        }
    }
    //This will return course by ID of course
    public Courses getCourseByiD(Long id){
        return courseRepository.findById(id).get();
    }
    //This will return all courses It is usefull only for admin to list all available courses
//    public List<Courses> getAllCourseReletedToProfessor(String username){
//        return courseRepository.getCoursesByUsername(username);
//    }
    public List<CourseDTO> getCoursesOfUser(Long id){
        List<CourseDTO> courseDTOList= courseRepository.getCourseNameAndIdAndCode(id);
        return courseDTOList;
    }
//TODO currently Deleting according to ID of course.NEED TO CHECK if
    //TODO course deleted with its syllabus or not.
    // it is not checking either course are deleting by it is owner or not.
    //What happens when we delete course and it is taken by someone there is relation  -> Done

    public void deleteCourse(Long courseID, String username) {
        if (courseRepository.findById(courseID).isPresent()){
            if (courseRepository.existsByCourse_idAndAndUserInfos(courseID, username)){
                entityManager.createNativeQuery("DELETE FROM course_user where course_id=?1")
                        .setParameter(1,courseID)
                        .executeUpdate();
                List<Syllabus> syllabusList = syllabusService.getAllSyllabus(courseID);
                syllabusService.deleteByCourseId(syllabusList);
                courseRepository.deleteById(courseID);
            }else {
                throw new IllegalStateException("You do not have an access to delete this course!");
            }
        }
        else {
            throw new IllegalStateException("Can't find course");
        }
    }
    //Professor can update some part of the course if it will pass course information
    public void updateCourse(CourseDTO courseDTO) {
        Courses existingCourse=getCourseByiD(courseDTO.getCourse_id());
        if (courseDTO.getName()!=null){
            existingCourse.setName(courseDTO.getName());}
        if (courseDTO.getCourseTypes()!=null){
            existingCourse.setCourseTypes(courseDTO.getCourseTypes());}
        if (courseDTO.getInfo()!=null){
            existingCourse.setInfo(courseDTO.getInfo());}
        if (courseDTO.getCourseStatus()!=null){
            existingCourse.setCourseStatus(courseDTO.getCourseStatus());}
        if (courseDTO.getCode()!=null){
            existingCourse.setCode(courseDTO.getCode());}
        courseRepository.save(existingCourse);
    }

    public boolean isProfessorWithId(Long id){
        return professorRepository.existsByRoleAndId(id);
    }

    public List<CourseDTO> getAllCourses(){
        return courseRepository.getAllCourses();
    }
}
