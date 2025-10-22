package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.EnrollmentStatus;
import raisetech.student.management.service.EnrollmentStatusService;

/**
 * 申込状況の検索や登録、更新を行うREST APIとして受け付けるController
 */
@Validated
@RestController
public class EnrollmentStatusController {

  private EnrollmentStatusService service;

  @Autowired
  public EnrollmentStatusController(EnrollmentStatusService service) {
    this.service = service;
  }

  /**
   * 申込状況の全件検索を行う。
   *
   * @return 申込状況の一覧(全件)
   */
  @Operation(summary = "申込状況一覧検索", description = "全ての申込状況を取得します。")
  @GetMapping("/enrollmentStatuses")
  public List<EnrollmentStatus> getAllStatuses() {
    return service.searchAllStatuses();
  }

  /**
   * IDによる申込状況の検索を行う。
   *
   * @param id 申込状況ID
   * @return 申込状況
   */
  @Operation(summary = "IDによる申込状況検索", description = "申込状況IDを指定して対象の申込状況を取得します。",
      parameters = {@Parameter(name = "id", description = "申込状況ID", example = "1")})
  @GetMapping("/enrollmentStatuses/{id}")
  public EnrollmentStatus getStatusById(@PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String id) {
    return service.searchStatusById(id);
  }

  /**
   * 受講生コースIDによる申込状況の検索を行う。
   *
   * @param studentCourseId 受講生コースID
   * @return 申込状況
   */
  @Operation(summary = "受講生コースIDによる申込状況検索",
      description = "受講生コースIDを指定して申込状況を取得します。",
      parameters = {@Parameter(name = "studentCourseId", description = "受講生コースID", example = "1")})
  @GetMapping("/studentCourses/{studentCourseId}/enrollmentStatus")
  public EnrollmentStatus getStatusByStudentCourseId(@PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String studentCourseId) {
    return service.searchStatusByStudentCourseId(studentCourseId);
  }

  /**
   * 申込状況の初期登録を行う。
   *
   * @param studentCourseId 受講生コースID
   * @return 実行結果
   */
  @Operation(summary = "申込状況初期登録", description = "申込状況を初期状態（仮申込）で登録します。",
      parameters = {@Parameter(name = "studentCourseId", description = "受講生コースID", example = "1")})
  @PostMapping("/studentCourses/{studentCourseId}/enrollmentStatus/initial")
  public ResponseEntity<EnrollmentStatus> registerInitial(@PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String studentCourseId) {
    EnrollmentStatus newStatus = service.registerInitialStatus(studentCourseId);
    return ResponseEntity.ok(newStatus);
  }

  /**
   * 申込状況の更新を行う。
   *
   * @param studentCourseId 受講生コースID
   * @param enrollmentStatus 申込状況
   * @return 実行結果
   */
  @Operation(summary = "申込状況更新", description = "既存の申込状況を更新します。",
      parameters = {@Parameter(name = "studentCourseId", description = "受講生コースID", example = "1")})
  @PutMapping("/studentCourses/{studentCourseId}/enrollmentStatus")
  public ResponseEntity<String> updateStatus(@PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String studentCourseId, @RequestBody @Valid EnrollmentStatus enrollmentStatus) {
    service.updateStatus(studentCourseId, enrollmentStatus);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * 本申込への昇格を行う。
   *
   * @param studentCourseId 受講生コースID
   * @return 実行結果
   */
  @Operation(summary = "本申込への昇格", description = "申込状況を「仮申込」から「本申込」に変更します。",
      parameters = {@Parameter(name = "studentCourseId", description = "受講生コースID", example = "1")})
  @PutMapping("/studentCourses/{studentCourseId}/enrollmentStatus/formal")
  public ResponseEntity<EnrollmentStatus> promoteToFormalEnrollment(@PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String studentCourseId) {
    EnrollmentStatus updatedStatus = service.promoteToFormalEnrollment(studentCourseId);
    return ResponseEntity.ok(updatedStatus);
  }
}
