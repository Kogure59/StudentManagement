package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるController
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  // 空文字もnull相当として扱う
  private boolean isEmpty(String s){
    return s == null || s.trim().isEmpty();
  }

  /**
   * 受講生詳細の条件指定検索。条件指定を行わない場合は、全件検索を行う。
   *
   * @return 受講生一覧(全件) (条件指定を行わない場合), @return 条件指定に該当する受講生詳細 (条件指定を行う場合)
   */
  @Operation(summary = "受講生詳細の条件指定検索", description = "受講生詳細を条件指定検索します。条件指定を行わない場合には、受講生の一覧を検索します。")
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String kanaName,
      @RequestParam(required = false) String nickname,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String area,
      @RequestParam(required = false) Integer age,
      @RequestParam(required = false) String gender,
      @RequestParam(required = false) String remark,
      @RequestParam(required = false) Boolean isDeleted,
      @RequestParam(required = false) String courseName,
      @DateTimeFormat(iso = ISO.DATE)
      @RequestParam(required = false) LocalDate courseStartAt,
      @DateTimeFormat(iso = ISO.DATE)
      @RequestParam(required = false) LocalDate courseEndAt
  ) {
    if (isEmpty(name) && isEmpty(kanaName) && isEmpty(nickname) && isEmpty(email) && isEmpty(area)
        && age == null && isEmpty(gender) && isEmpty(remark) && isDeleted == null
        && isEmpty(courseName) && courseStartAt == null && courseEndAt == null) {
      return service.searchStudentList();
    }
    return service.searchStudentListByCondition(name, kanaName, nickname, email, area, age, gender, remark, isDeleted, courseName, courseStartAt, courseEndAt);
  }

  /**
   * 受講生詳細の検索。IDに紐づく任意の受講生の情報を取得する。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @Operation(summary = "受講生詳細検索", description = "受講生IDを指定して、対象の受講生の詳細情報を取得します。",
             parameters = {@Parameter(name = "id", description = "受講生ID", example = "1")})
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の登録を行う。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生登録", description = "受講生を登録します。")
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行う。キャンセルフラグの更新もここで行う。(論理削除)
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生更新", description = "受講生の詳細情報を更新します。")
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  @GetMapping("/exception")
  public  ResponseEntity<String> throwException() throws NotFoundException {
    throw new NotFoundException("このAPIは現在利用できません。古いURLとなっています。");
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handlerNotFoundException(NotFoundException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}
