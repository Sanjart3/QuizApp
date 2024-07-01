package com.example.quizbackend.questions.exams.qpaper;

import com.example.quizbackend.courses.Courses;
import com.example.quizbackend.questions.exams.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ExamPapersRepository extends JpaRepository<ExamPapers, Long> {
    @Query(value = "SELECT e.* from exam_papers e where e.courses_course_id=?1 and e.exam_type=?2 ",nativeQuery = true)
    List<ExamPapers> findByCoursesAndAndExamType(Long coursID, String type);

    @Query(value = "SELECT e.* from exam_papers e where e.courses_course_id=?1 and e.exam_type=?2 and e.exam_taker is null and e.status=true",nativeQuery = true)
    ExamPapers  findByCoursAndAndExamTypeAndStatus(Long courseID,String examType);

    @Query(value = "SELECT e.* from exam_papers e where e.courses_course_id=?1 and e.exam_type=?2 and e.exam_taker is null ",nativeQuery = true)
    List<ExamPapers> findPapers(Long courseID, String examType);
    @Query(value = "SELECT e.* from exam_papers e where e.courses_course_id=?1 and e.exam_type=?2 and e.exam_taker =?3 and e.status=true",nativeQuery = true)

    ExamPapers findByExamTaker(Long CourseId,String examType,String examtaker);
List<ExamPapers> findByUniqueExamName(String name);
    @Query(value = "SELECT e.* from exam_papers e where e.unique_exam_name=?1 and e.exam_taker is not null",nativeQuery = true)
    List<ExamPapers> findPapersToCheck(String name);

    ExamPapers findExamPapersByUniqueExamNameAndAndId(String name, Long id);

    @Query(value = "SELECT * from exam_papers e where e.exam_taker=?1 AND  e.exam_type != 'RANDOM' ",nativeQuery = true)
    List<ExamPapers> findByStudent(String username);
    ExamPapers findExamPapersById(Long id);

}