package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.EnrollmentStatus;

@MybatisTest
class EnrollmentStatusRepositoryTest {

  @Autowired
  private EnrollmentStatusRepository sut;

  // 申込状況の全件検索が行えること
  @Test
  void searchAll_whenCalled_thenReturnAllEnrollmentStatuses() {
    List<EnrollmentStatus> statuses = sut.searchAll();

    assertThat(statuses.size()).isEqualTo(6);
  }

  // IDによって申込状況の検索が行えること
  @Test
  void searchById_whenValidIdGiven_thenReturnExpectedStatus() {
    EnrollmentStatus status = sut.searchById("1");

    assertThat(status.getStatus()).isEqualTo("受講終了");
  }

  // 受講生コース情報に紐づく申込状況の検索が行えること
  @Test
  void searchByStudentCourseId_whenValidIdGiven_thenReturnExpectedStatus() {
    EnrollmentStatus status = sut.searchByStudentCourseId("3");

    assertThat(status.getStatus()).isEqualTo("受講中");
  }

  // 申込状況の登録が行えること
  @Test
  void registerStatus_whenValidDataGiven_thenStatusIsRegistered() {
    EnrollmentStatus enrollmentStatus = new EnrollmentStatus();
    enrollmentStatus.setStudentCourseId("1");
    enrollmentStatus.setStatus("仮申込");

    sut.registerStatus(enrollmentStatus);

    assertThat(enrollmentStatus.getId()).isNotNull();
  }

  // 申込状況の更新が行えること
  @Test
  void updateStatus_whenExistingStatusUpdated_thenChangesAreSaved() {
    EnrollmentStatus enrollmentStatus = new EnrollmentStatus();
    enrollmentStatus.setStudentCourseId("1");
    enrollmentStatus.setStatus("仮申込");
    sut.registerStatus(enrollmentStatus);

    EnrollmentStatus registered = sut.searchById(enrollmentStatus.getId());

    String newStatus = "本申込";
    registered.setStatus(newStatus);

    sut.updateStatus(registered);

    EnrollmentStatus updated = sut.searchById(registered.getId());

    assertThat(updated.getStatus()).isEqualTo(newStatus);
   }
}