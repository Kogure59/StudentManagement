package raisetech.student.management.repository;

import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepository
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行う。
   *
   * @return 受講生一覧(全件)
   */
  List<Student> search();

  /**
   * 受講生の条件検索を行う。
   *
   * @param name 名前
   * @param kanaName カナ名
   * @param nickname ニックネーム
   * @param email メールアドレス
   * @param area 地域
   * @param age 年齢
   * @param gender 性別
   * @param remark 備考
   * @param isDeleted 論理削除フラグ
   * @return 受講生一覧
   */
  List<Student> searchStudentByCondition(
      String name,
      String kanaName,
      String nickname,
      String email,
      String area,
      Integer age,
      String gender,
      String remark,
      Boolean isDeleted
  );

  /**
   * 受講生の検索を行う。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student searchStudent(String id);

  /**
   * 受講生のコース情報の全件検索を行う。
   *
   * @return 受講生コース情報(全件)
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生のコース情報の条件検索を行う。
   *
   * @param courseName 受講生コース名
   * @param courseStartAt 受講開始日(yyyy-MM-dd)
   * @param courseEndAt 受講終了日(yyyy-MM-dd)
   * @return 受講生コース情報一覧
   */
  List<StudentCourse> searchStudentCourseByCondition(
      String courseName,
      LocalDate courseStartAt,
      LocalDate courseEndAt
  );

  /**
   * 受講生IDに紐づく受講生コース情報を検索する。
   *
   * @param studentId 受講生ID
   * @return 受講生に紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourse(String studentId);

  /**
   * 受講生を新規登録する。IDに関しては自動採番を行う。
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録する。IDに関しては自動採番を行う。
   *
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新する。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新する。
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);
}
