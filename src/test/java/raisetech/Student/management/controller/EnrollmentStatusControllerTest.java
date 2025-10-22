package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.EnrollmentStatus;
import raisetech.student.management.service.EnrollmentStatusService;

@WebMvcTest(EnrollmentStatusController.class)
class EnrollmentStatusControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  private EnrollmentStatusService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  // 申込状況の全件検索が実行できて空のリストが返ってくること
  @Test
  void getAllStatuses_whenCalled_thenReturnEmptyList() throws Exception {
    mockMvc.perform(get("/enrollmentStatuses"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchAllStatuses();
  }

  // IDによる申込状況の検索が実行できて結果が返ってくること
  @Test
  void getStatusById_whenValidIdGiven_thenReturnExpectedStatus() throws Exception {
    String id = "1";
    String status = "仮申込";
    EnrollmentStatus enrollmentStatus = new EnrollmentStatus();
    enrollmentStatus.setId(id);
    enrollmentStatus.setStatus(status);
    when(service.searchStatusById(id)).thenReturn(enrollmentStatus);

    mockMvc.perform(get("/enrollmentStatuses/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.status").value(status));

    verify(service, times(1)).searchStatusById(id);
  }

  // 受講生コースIDによる申込状況の検索が実行できて結果が返ってくること
  @Test
  void getStatusByStudentCourseId_whenValidIdGiven_thenReturnExpectedStatus() throws Exception {
    String studentCourseId = "1001";
    String status = "仮申込";
    EnrollmentStatus enrollmentStatus = new EnrollmentStatus();
    enrollmentStatus.setStudentCourseId(studentCourseId);
    enrollmentStatus.setStatus(status);
    when(service.searchStatusByStudentCourseId(studentCourseId)).thenReturn(enrollmentStatus);

    mockMvc.perform(get("/studentCourses/{studentCourseId}/enrollmentStatus", studentCourseId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.studentCourseId").value(studentCourseId))
        .andExpect(jsonPath("$.status").value(status));

    verify(service, times(1)).searchStatusByStudentCourseId(studentCourseId);
  }

  // 初期申込登録が実行できること
  @Test
  void registerInitial_whenCalled_thenServiceMethodIsExecuted() throws Exception {
    String studentCourseId = "1001";
    mockMvc.perform(post("/studentCourses/{studentCourseId}/enrollmentStatus/initial", studentCourseId))
           .andExpect(status().isOk());

    verify(service, times(1)).registerInitialStatus(studentCourseId);
  }

  // 申込状況の更新が実行できること
  @Test
  void updateStatus_whenValidRequestGiven_thenUpdateIsPerformed() throws Exception {
    String studentCourseId ="1001";
    String jsonBody = """
        {
          "id": "1",
          "studentCourseId": "1001",
          "status": "受講終了"
        }
        """;
    mockMvc.perform(put("/studentCourses/{studentCourseId}/enrollmentStatus", studentCourseId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
           .andExpect(status().isOk());

    verify(service, times(1)).updateStatus(anyString(), any(EnrollmentStatus.class));
  }

  // 本申込への昇格が実行できること
  @Test
  void promoteToFormalEnrollment_whenCalled_thenServiceMethodIsExecuted() throws Exception {
    String studentCourseId = "1001";
    mockMvc.perform(put("/studentCourses/{studentCourseId}/enrollmentStatus/formal", studentCourseId))
        .andExpect(status().isOk());

    verify(service, times(1)).promoteToFormalEnrollment(studentCourseId);
  }

  // 申込状況で適切な値を入力した時に入力チェックに異常が発生しないこと
  @Test
  void validateEnrollmentStatus_whenValidInputGiven_thenNoViolationErrors() {
    EnrollmentStatus enrollmentStatus = new EnrollmentStatus();
    enrollmentStatus.setId("1");
    enrollmentStatus.setStudentCourseId("1");
    enrollmentStatus.setStatus("仮申込");

    Set<ConstraintViolation<EnrollmentStatus>> violations = validator.validate(enrollmentStatus);

    assertThat(violations.size()).isEqualTo(0);
  }

  // 申込状況でIDに数字以外を入力した時に入力チェックに掛かること
  @Test
  void validateEnrollmentStatus_whenNonNumericIdGiven_thenValidationFails() {
    EnrollmentStatus enrollmentStatus = new EnrollmentStatus();
    enrollmentStatus.setId("テストです。");
    enrollmentStatus.setStudentCourseId("テストです。");
    enrollmentStatus.setStatus("仮申込");

    Set<ConstraintViolation<EnrollmentStatus>> violations = validator.validate(enrollmentStatus);

    assertThat(violations.size()).isEqualTo(2);
    assertThat(violations)
        .extracting(ConstraintViolation::getPropertyPath)
        .extracting(Object::toString)
        .containsExactlyInAnyOrder("id", "studentCourseId");
  }

  // 申込状況で値を空にしたときに入力チェックに掛かること
  @Test
  void validateEnrollmentStatus_whenStatusIsEmpty_thenValidationFails() {
    EnrollmentStatus enrollmentStatus = new EnrollmentStatus();
    enrollmentStatus.setId("1");
    enrollmentStatus.setStudentCourseId("1");
    enrollmentStatus.setStatus("");

    Set<ConstraintViolation<EnrollmentStatus>> violations = validator.validate(enrollmentStatus);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations)
        .extracting(ConstraintViolation::getPropertyPath)
        .extracting(Object::toString)
        .contains("status");
  }
}