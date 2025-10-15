package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.student.management.data.EnrollmentStatus;

/**
 * 申込状況テーブルと紐づくRepository
 */
@Mapper
public interface EnrollmentStatusRepository {

  /**
   * 申込状況の全件検索を行う。
   *
   * @return 申込状況の一覧(全件)
   */
  List<EnrollmentStatus> searchAll();

  /**
   * IDによる申込状況の検索を行う。
   *
   * @param id 申込状況ID
   * @return 申込状況
   */
  EnrollmentStatus searchById(String id);

  /**
   * 受講生コースIDによる申込状況の検索を行う。
   *
   * @param studentCourseId 受講生コースID
   * @return 申込状況
   */
  EnrollmentStatus searchByStudentCourseId(String studentCourseId);

  /**
   * 申込状況の登録を行う。
   *
   * @param enrollmentStatus 申込状況
   */
  void registerStatus(EnrollmentStatus enrollmentStatus);

  /**
   * 申込状況の更新を行う。
   *
   * @param enrollmentStatus 申込状況
   */
  void updateStatus(EnrollmentStatus enrollmentStatus);
}
