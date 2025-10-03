package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細検索が実行できて空で返ってくること() throws Exception {
    String id = "123";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の登録が実行できて空で返ってくること() throws Exception {
    String jsonBody = """
        {
            "student": {
                "name": "江並公史",
                "kanaName": "エナミコウジ",
                "nickname": "コウジ" ,
                "email": "test@example.com",
                "area": "奈良県",
                "age": "36",
                "gender": "男性",
                "remark": ""
            },
            "studentCourseList" : [
                {
                    "courseName": "Javaコース"
                }
            ]
        }
        """;

    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の更新が実行できて空で返ってくること() throws Exception {
    String jsonBody = """
        {
            "student": {
                "id": "13",
                "name": "中田英寿",
                "kanaName": "ナカタヒデトシ",
                "nickname": "ヒデ" ,
                "email": "test@example.com",
                "area": "山梨県",
                "age": "48",
                "gender": "男性",
                "remark": "",
                "isDeleted": "false"
            },
            "studentCourseList" : [
                    {
                        "id": "8",
                        "studentId": "13",
                        "courseName": "Javaコース",
                        "courseStartAt": "2025-09-01T00:12:21",
                        "courseEndAt": "2026-09-01T00:12:21"
                    }
                ]
        }
        """;
    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON).content(jsonBody))
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました。"));

    verify(service, times(1)).updateStudent(any());
  }

  @Test
  void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
    mockMvc.perform(get("/exception"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId("1");
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmail("test@example.com");
    student.setArea("奈良県");
    student.setGender("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
    Student student = new Student();
    student.setId("テストです。");
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmail("test@example.com");
    student.setArea("奈良県");
    student.setGender("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").contains("数字のみ入力するようにしてください");
  }

  @Test
  void 受講生詳細の受講生で値を空にしたときに入力チェックに掛かること() {
    Student student = new Student();
    student.setId("1");
    student.setName("");
    student.setKanaName("");
    student.setNickname("");
    student.setEmail("");
    student.setArea("");
    student.setGender("");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(6);
    assertThat(violations).extracting("message").contains("空白は許可されていません");
  }


  @Test
  void 受講生詳細の受講生コース情報で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("Javaコース");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生コース情報でIDに数字以外を用いた時に入力チェックに掛かること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("テストです。");
    studentCourse.setStudentId("テストです。");
    studentCourse.setCourseName("Javaコース");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(2);
    assertThat(violations).extracting("message").contains("数字のみ入力するようにしてください");
  }

  @Test
  void 受講生詳細の受講生コース情報で値を空にしたときに入力チェックに掛かること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").contains("空白は許可されていません");
  }
}