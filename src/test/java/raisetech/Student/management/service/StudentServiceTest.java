package raisetech.student.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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

  // 受講生詳細の一覧検索で、RepositoryとConverterの処理が正しく呼び出されること
  @Test
  void searchStudentList_callsRepositoryAndConverterProperly() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  // 条件指定受講生検索で、コース条件に一致する受講生のみが返ること
  @Test
  void searchStudentListByCondition_returnsOnlyStudentsMatchingCourseCondition() {
    // 準備
    Student student1 = new Student();
    student1.setId("1");
    Student student2 = new Student();
    student2.setId("2");
    List<Student> matchedStudents = List.of(student1, student2);

    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setStudentId("1");
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setStudentId("2");
    List<StudentCourse> allCourses = List.of(studentCourse1, studentCourse2);

    // コース条件に合致するのは student1 のみ
    StudentCourse matchedCourse = new StudentCourse();
    matchedCourse.setStudentId("1");
    List<StudentCourse> matchedCourses = List.of(matchedCourse);
    String name = "山田太郎";
    String courseName = "Javaコース";
    when(repository.searchStudentByCondition(
        name, null, null, null, null, null, null, null, null)
    ).thenReturn(matchedStudents);
    when(repository.searchStudentCourseList()).thenReturn(allCourses);
    when(repository.searchStudentCourseByCondition(courseName, null, null)).thenReturn(matchedCourses);

    // コース条件に合致する student1 のみがフィルタに通る
    List<Student> filteredStudents = List.of(student1);
    List<StudentDetail> expected = List.of(new StudentDetail(student1, List.of()));
    when(converter.convertStudentDetails(filteredStudents, allCourses)).thenReturn(expected);

    // 実行
    List<StudentDetail> actual = sut.searchStudentListByCondition(
        name, null, null, null, null, null, null, null, null,
        courseName, null, null);

    // 検証
    verify(repository, times(1)).searchStudentByCondition(
        name, null, null, null, null, null, null, null, null);
    verify(repository, times(1)).searchStudentCourseList();
    verify(repository, times(1)).searchStudentCourseByCondition(courseName, null, null);
    verify(converter, times(1)).convertStudentDetails(filteredStudents, allCourses);
    assertSame(expected, actual);
  }

  // 条件指定受講生検索で、条件指定なしの場合は全件返ること
  @Test
  void searchStudentListByCondition_returnsAllStudentsWhenNoConditionSpecified() {
    // 準備
    List<Student> students = List.of(new Student(), new Student());
    List<StudentCourse> allCourses = List.of(new StudentCourse(), new StudentCourse());
    List<StudentCourse> matchedCourses = new ArrayList<>();
    when(repository.searchStudentByCondition(
        null, null, null, null, null, null, null, null, null)
    ).thenReturn(students);
    when(repository.searchStudentCourseList()).thenReturn(allCourses);
    when(repository.searchStudentCourseByCondition(null, null, null)).thenReturn(matchedCourses);

    List<StudentDetail> expected = List.of(new StudentDetail(students.get(0), List.of()), new StudentDetail(students.get(1), List.of()));
    when(converter.convertStudentDetails(students, allCourses)).thenReturn(expected);

    // 実行
    List<StudentDetail> actual = sut.searchStudentListByCondition(
        null, null, null, null, null, null, null, null, null,
        null, null, null);

    // 検証
    verify(repository, times(1)).searchStudentByCondition(
        null, null, null, null, null, null, null, null, null);
    verify(repository, times(1)).searchStudentCourseList();
    verify(repository, times(1)).searchStudentCourseByCondition(null, null, null);
    verify(converter, times(1)).convertStudentDetails(students, allCourses);
    assertSame(expected, actual);
  }

  // コース条件が指定されているが該当なしの時、空のリストを返すこと
  @Test
  void shouldReturnEmptyList_whenCourseConditionSpecifiedButNoMatch() {
    String courseName = "存在しないコース";

    List<StudentDetail> result = sut.searchStudentListByCondition(
        null, null, null, null, null, null, null, null, null,
        courseName, null, null);

    assertThat(result).isEmpty();
  }

  // 受講生詳細検索で、Repositoryの呼び出しと戻り値が正しいこと
  @Test
  void searchStudent_returnsExpectedStudentDetail() {
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

  // 受講生詳細登録で、Repositoryの呼び出しが正しく、戻り値が入力値と同一であること
  @Test
  void registerStudent_callsRepositoryProperlyAndReturnsInput() {
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

  // 受講生コース登録時、受講生IDと受講期間が正しく初期化されること
  @Test
  void initStudentsCourse_setsStudentIdAndCoursePeriodProperly() {
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

  // 受講生詳細更新で、Repositoryの呼び出しが正しく行われること
  @Test
  void updateStudent_callsRepositoryProperly() {
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