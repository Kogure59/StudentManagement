package raisetech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.EnrollmentStatus;
import raisetech.student.management.repository.EnrollmentStatusRepository;

/**
 * 申込状況を取り扱うサービス
 * 申込状況の検索や登録・更新処理を行う。
 */
@Service
public class EnrollmentStatusService {

  /** 申込状況の初期状態を表す文字列。 */
  private static final String STATUS_PROVISIONAL = "仮申込";

  /** 本申込へ昇格した状態を表す文字列。 */
  private static final String STATUS_FORMAL = "本申込";

  private EnrollmentStatusRepository repository;

  @Autowired
  public EnrollmentStatusService(EnrollmentStatusRepository repository) {
    this.repository = repository;
  }

  /**
   * 申込状況の全件検索を行う。
   *
   * @return 申込状況の一覧(全件)
   */
  public List<EnrollmentStatus> searchAllStatuses() {
    return repository.searchAll();
  }

  /**
   * IDによる申込状況の検索を行う。
   *
   * @param id 申込状況ID
   * @return 申込状況
   */
  public EnrollmentStatus searchStatusById(String id) {
    return repository.searchById(id);
  }

  /**
   * 受講生コースIDによる申込状況の検索を行う。
   *
   * @param studentCourseId 受講生コースID
   * @return 申込状況
   */
  public EnrollmentStatus searchStatusByStudentCourseId(String studentCourseId) {
    return repository.searchByStudentCourseId(studentCourseId);
  }

  /**
   * 申込状況を初期状態「仮申込」で登録を行う。
   *
   * @param studentCourseId 受講生コースID
   * @return 登録情報を付与した申込状況
   */
  public EnrollmentStatus registerInitialStatus(String studentCourseId) {
    EnrollmentStatus status = new EnrollmentStatus();
    status.setStudentCourseId(studentCourseId);
    status.setStatus(STATUS_PROVISIONAL);
    repository.registerStatus(status);
    return status;
  }

  /**
   * 申込状況の更新を行う。
   *
   * @param enrollmentStatus 申込状況
   */
  public void updateStatus(String studentCourseId, EnrollmentStatus enrollmentStatus) {
    enrollmentStatus.setStudentCourseId(studentCourseId);
    repository.updateStatus(enrollmentStatus);
  }

  /**
   * 本申込への昇格を行う。
   *
   * @param studentCourseId 受講生コースID
   * @return 更新された申込状況
   */
  public EnrollmentStatus promoteToFormalEnrollment(String studentCourseId) {
    EnrollmentStatus status = repository.searchByStudentCourseId(studentCourseId);
    if (status != null) {
      status.setStatus(STATUS_FORMAL);
      repository.updateStatus(status);
    }
    return status;
  }
}
