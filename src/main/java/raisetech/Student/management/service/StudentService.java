package raisetech.Student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.Student.management.controller.converter.StudentConverter;
import raisetech.Student.management.data.Student;
import raisetech.Student.management.data.StudentsCourses;
import raisetech.Student.management.domain.StudentDetail;
import raisetech.Student.management.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービス
 * 受講生の検索や登録・更新処理を行う。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {

    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生一覧検索
   * 全件検索を行うので、条件指定は行わない。
   *
   * @return 受講生一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentsCourses> studentsCoursesList = repository.searchStudentsCoursesList();
    return converter.convertStudentDetails(studentList, studentsCoursesList);
  }

  /**
   * 受講生検索
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定する。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentsCourses(student.getId());
    return new StudentDetail(student, studentsCourses);
  }

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
      studentsCourse.setStudentId(studentDetail.getStudent().getId());
      studentsCourse.setCourseStartAt(LocalDateTime.now());
      studentsCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));
      repository.registerStudentsCourses(studentsCourse);
    }
    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
      repository.updateStudentsCourses(studentsCourse);
    }
  }
}
