package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void 受講生詳細検索_リポジトリの呼び出しと戻り値が適切であること() {
    Student student = new Student();
    student.setId("123");
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentDetail expected = new StudentDetail(student, studentCourseList);

    when(repository.searchStudent("123")).thenReturn(student);
    when(repository.searchStudentCourse("123")).thenReturn(studentCourseList);

    StudentDetail result = sut.searchStudent("123");

    verify(repository, times(1)).searchStudent("123");
    verify(repository, times(1)).searchStudentCourse("123");

    assertEquals(expected.getStudent(), result.getStudent());
    assertEquals(expected.getStudentCourseList(), result.getStudentCourseList());
  }

  @Test
  void 受講生詳細の登録_リポジトリの呼び出しが適切で_戻り値が入力値と等しいこと() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse);
    StudentDetail input = new StudentDetail(student, studentCourseList);

    StudentDetail result = sut.registerStudent(input);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);

    assertSame(input, result);
  }

  @Test
  void 受講生コース情報を登録する際の初期化_受講生IDと受講開始日_受講終了日が正しく設定されること() {
    Student student = new Student();
    student.setId("123");
    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student);

    assertEquals("123", studentCourse.getStudentId());

    LocalDateTime now = LocalDateTime.now();

    assertTrue(!studentCourse.getCourseStartAt().isBefore(now.minusSeconds(5))
        && !studentCourse.getCourseStartAt().isAfter(now.plusSeconds(5)), "受講開始日が現在時刻付近(±5秒の範囲内)であること");

    assertEquals(studentCourse.getCourseStartAt().plusYears(1),
        studentCourse.getCourseEndAt(),"受講終了日は受講開始日の1年後であること");
  }

  @Test
  void 受講生詳細の更新_リポジトリの呼び出しが適切であること() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse);
    StudentDetail input = new StudentDetail(student, studentCourseList);

    sut.updateStudent(input);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }
}