package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  // 受講生の全件検索が行えること
  @Test
  void searchStudentList_whenCalled_returnsAllStudents() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  // 受講生の条件検索が行えること(例:名前で検索)
  @Test
  void searchStudentByCondition_givenName_returnsMatchingStudents() {
    String name = "山田太郎";

    List<Student> actual = sut.searchStudentByCondition(
        name, null, null, null, null, null, null, null, null
    );

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0).getName()).isEqualTo(name);
  }

  // 受講生の条件検索が行えること(条件指定なし = 全件取得相当)
  @Test
  void searchStudentByCondition_givenNoCondition_returnsAllStudents() {
    List<Student> actual = sut.searchStudentByCondition(
        null, null, null, null, null, null, null, null, null
    );

    assertThat(actual).hasSize(5);
  }

  // 受講生の検索が行えること
  @Test
  void searchStudent_givenId_returnsCorrectStudent() {
    String id = "1";

    Student actual = sut.searchStudent(id);

    assertThat(actual.getId()).isEqualTo(id);
  }

  // 受講生のコース情報の全件検索が行えること
  @Test
  void searchStudentCourseList_whenCalled_returnsAllCourses() {
    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(6);
  }

  // 受講生のコース情報の条件検索が行えること(例:コース名で検索)
  @Test
  void searchStudentCourseByCondition_givenCourseName_returnsMatchingCourses() {
    String courseName = "Javaコース";

    List<StudentCourse> actual = sut.searchStudentCourseByCondition(
        courseName, null, null
    );

    assertThat(actual).isNotEmpty();
    assertThat(actual).allMatch(course -> course.getCourseName().contains(courseName));
  }

  // 受講生のコース情報の条件検索が行えること(条件指定なし = 全件取得相当)
  @Test
  void searchStudentCourseByCondition_givenNoCondition_returnsAllCourses() {
    List<StudentCourse> actual = sut.searchStudentCourseByCondition(
        null, null, null
    );

    assertThat(actual.size()).isEqualTo(6);
  }

  // course_start_at >= 2025-10-01 の条件で検索すると10月以降のコースのみ取得できること
  @Test
  void shouldReturnCoursesStartingAfterGivenDate() {
    LocalDate startAt = LocalDate.of(2025, 10, 1);

    List<StudentCourse> result = sut.searchStudentCourseByCondition(null, startAt, null);

    assertThat(result.size()).isEqualTo(2);
  }

  // course_end_at <= 2025-10-30 の条件で検索すると10月以前に終了するコースのみ取得できること
  @Test
  void shouldReturnCoursesEndingBeforeGivenDate() {
    LocalDate endAt = LocalDate.of(2025, 10, 30);

    List<StudentCourse> result = sut.searchStudentCourseByCondition(null, null, endAt);

    assertThat(result.size()).isEqualTo(4);
  }

  // course_start_at >= 2025-09-01 かつ course_end_at <= 2025-10-01 の条件で検索すると特定のコースのみ取得できること
  @Test
  void shouldReturnCoursesWithinGivenDateRange() {
    LocalDate startAt = LocalDate.of(2025, 9, 1);
    LocalDate endAt = LocalDate.of(2025, 10, 1);

    List<StudentCourse> result = sut.searchStudentCourseByCondition(null, startAt, endAt);

    assertThat(result.size()).isEqualTo(1);
  }

  // 受講生IDに紐づく受講生コース情報の検索が行えること
  @Test
  void searchStudentCourse_givenStudentId_returnsCorrectCourses() {
    String studentId = "2";

    List<StudentCourse> actual = sut.searchStudentCourse(studentId);

    assertThat(actual.size()).isEqualTo(2);
  }

  // 受講生の登録が行えること
  @Test
  void registerStudent_whenNewStudentProvided_increasesStudentCount() {
    Student student = exampleStudent();

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);
  }

  // 受講生コース情報の登録が行えること
  @Test
  void registerStudentCourse_whenNewCourseProvided_increasesCourseCount() {
    Student student = exampleStudent();
    sut.registerStudent(student);

    StudentCourse studentCourse = exampleStudentCourse(student);

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(7);
  }

  // 受講生の更新が行えること
  @Test
  void updateStudent_whenStudentUpdated_updatesStudentInfo() {
    Student student = exampleStudent();
    sut.registerStudent(student);

    String name = "真司香川";
    String kanaName = "シンジカガワ";
    String nickname = "カガワ";
    String email = "test_updated@example.com";
    String area = "大阪府";
    int age = 37;
    String gender = "その他";
    String remark = "備考テスト";
    student.setName(name);
    student.setKanaName(kanaName);
    student.setNickname(nickname);
    student.setEmail(email);
    student.setArea(area);
    student.setAge(age);
    student.setGender(gender);
    student.setRemark(remark);
    student.setDeleted(true);

    sut.updateStudent(student);

    Student actual = sut.searchStudent(student.getId());

    assertThat(actual.getName()).isEqualTo(name);
    assertThat(actual.getKanaName()).isEqualTo(kanaName);
    assertThat(actual.getNickname()).isEqualTo(nickname);
    assertThat(actual.getEmail()).isEqualTo(email);
    assertThat(actual.getArea()).isEqualTo(area);
    assertThat(actual.getAge()).isEqualTo(age);
    assertThat(actual.getGender()).isEqualTo(gender);
    assertThat(actual.getRemark()).isEqualTo(remark);
    assertThat(actual.isDeleted()).isTrue();
  }

  // 受講生コース情報の更新が行えること
  @Test
  void updateStudentCourse_whenCourseUpdated_updatesCourseInfo() {
    Student student = exampleStudent();
    sut.registerStudent(student);

    StudentCourse studentCourse = exampleStudentCourse(student);
    sut.registerStudentCourse(studentCourse);

    String courseName = "Webマーケティングコース";
    studentCourse.setCourseName(courseName);

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse(student.getId());

    assertThat(actual).anyMatch(studentCourse1 -> studentCourse1.getCourseName().equals(courseName));
  }

  private static Student exampleStudent() {
    Student student = new Student();
    student.setName("香川真司");
    student.setKanaName("カガワシンジ");
    student.setNickname("シンジ");
    student.setEmail("test@example.com");
    student.setArea("兵庫県");
    student.setAge(36);
    student.setGender("男性");
    student.setRemark("");
    student.setDeleted(false);
    return student;
  }

  private static StudentCourse exampleStudentCourse(Student student) {
    LocalDateTime localDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(student.getId());
    studentCourse.setCourseName("Javaコース");
    studentCourse.setCourseStartAt(localDateTime);
    studentCourse.setCourseEndAt(localDateTime.plusYears(1));
    return studentCourse;
  }
}