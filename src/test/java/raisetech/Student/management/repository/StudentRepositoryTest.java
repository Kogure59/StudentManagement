package raisetech.Student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.Student.management.data.Student;
import raisetech.Student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生の検索が行えること() {
    String id = "1";

    Student actual = sut.searchStudent(id);

    assertThat(actual.getId()).isEqualTo(id);
  }

  @Test
  void 受講生のコース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生IDに紐づく受講生コース情報の検索が行えること() {
    String studentId = "2";

    List<StudentCourse> actual = sut.searchStudentCourse(studentId);

    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = exampleStudent();

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生コース情報の登録が行えること() {
    Student student = exampleStudent();
    sut.registerStudent(student);

    StudentCourse studentCourse = exampleStudentCourse(student);

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(7);
  }

  @Test
  void 受講生の更新が行えること() {
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

  @Test
  void 受講生コース情報の更新が行えること() {
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
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(student.getId());
    studentCourse.setCourseName("Javaコース");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));
    return studentCourse;
  }
}