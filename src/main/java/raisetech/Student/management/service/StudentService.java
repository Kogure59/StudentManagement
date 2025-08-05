package raisetech.Student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.Student.management.data.Student;
import raisetech.Student.management.data.StudentCourse;
import raisetech.Student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {

    this.repository = repository;
  }

  /**
   * 年齢が30代の人のみを抽出する
   */
  public List<Student> searchStudentList() {
    return repository.search().stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
        .toList();
  }

  /**
   * 「Java」のコース情報のみを抽出する
   */
  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchStudentCourse().stream()
        .filter(studentCourse -> "Java" .equals(studentCourse.getCourseName()))
        .toList();
  }
}
