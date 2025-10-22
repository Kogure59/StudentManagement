package raisetech.student.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.data.EnrollmentStatus;
import raisetech.student.management.repository.EnrollmentStatusRepository;

@ExtendWith(MockitoExtension.class)
class EnrollmentStatusServiceTest {

  @Mock
  private EnrollmentStatusRepository repository;

  private EnrollmentStatusService sut;

  @BeforeEach
  void before() {
    sut = new EnrollmentStatusService(repository);
  }

  // 申込状況の全件検索_リポジトリの呼び出しが行われること
  @Test
  void searchAllStatuses_whenCalled_thenRepositoryIsInvoked() {
    List<EnrollmentStatus> statusList = new ArrayList<>();
    when(repository.searchAll()).thenReturn(statusList);

    sut.searchAllStatuses();

    verify(repository, times(1)).searchAll();
  }

  // IDによる申込状況の検索_リポジトリの呼び出しが行われること
  @Test
  void searchStatusById_whenValidIdGiven_thenReturnExpectedStatus() {
    String id = "1";
    EnrollmentStatus enrollmentStatus = new EnrollmentStatus();
    when(repository.searchById(id)).thenReturn(enrollmentStatus);

    EnrollmentStatus actual = sut.searchStatusById(id);

    verify(repository, times(1)).searchById(id);
    assertThat(actual).isSameAs(enrollmentStatus);
  }

  // 受講生コースIDによる申込状況の検索_リポジトリの呼び出しが行われること
  @Test
  void searchStatusByStudentCourseId_whenValidIdGiven_thenReturnExpectedStatus() {
    String studentCourseId = "1";
    EnrollmentStatus enrollmentStatus = new EnrollmentStatus();
    when(repository.searchByStudentCourseId(studentCourseId)).thenReturn(enrollmentStatus);

    EnrollmentStatus actual = sut.searchStatusByStudentCourseId(studentCourseId);

    verify(repository, times(1)).searchByStudentCourseId(studentCourseId);
    assertThat(actual).isSameAs(enrollmentStatus);
  }

  // 初期申込状況の登録_リポジトリの呼び出しが行われること
  @Test
  void registerInitialStatus_whenCalled_thenRepositoryRegistersDefaultStatus() {
    String studentCourseId = "1001";

    sut.registerInitialStatus(studentCourseId);

    ArgumentCaptor<EnrollmentStatus> captor = ArgumentCaptor.forClass(EnrollmentStatus.class);
    verify(repository, times(1)).registerStatus(captor.capture());

    EnrollmentStatus captured = captor.getValue();
    assertThat(captured.getStudentCourseId()).isEqualTo(studentCourseId);
    assertThat(captured.getStatus()).isEqualTo("仮申込");
  }

  // 申込状況の更新_リポジトリの呼び出しが行われること
  @Test
  void updateStatus_whenCalled_thenRepositoryUpdatesWithStudentCourseId() {
    String studentCourseId = "1001";
    EnrollmentStatus enrollmentStatus = new EnrollmentStatus();

    sut.updateStatus(studentCourseId, enrollmentStatus);

    verify(repository, times(1)).updateStatus(enrollmentStatus);

    assertThat(enrollmentStatus.getStudentCourseId()).isEqualTo(studentCourseId);
  }

  // 申込状況が存在する場合_本申込に更新_リポジトリの呼び出しが行われること
  @Test
  void promoteToFormalEnrollment_whenStatusExists_thenStatusIsUpdatedToFormal() {
    String studentCourseId = "1001";
    EnrollmentStatus existingStatus = new EnrollmentStatus();
    existingStatus.setStudentCourseId(studentCourseId);
    existingStatus.setStatus("仮申込");
    when(repository.searchByStudentCourseId(studentCourseId)).thenReturn(existingStatus);

    sut.promoteToFormalEnrollment(studentCourseId);

    assertThat(existingStatus.getStatus()).isEqualTo("本申込");
    verify(repository, times(1)).updateStatus(existingStatus);
  }

  // 申込状況が存在しない場合_更新処理が呼ばれないこと
  @Test
  void promoteToFormalEnrollment_whenStatusDoesNotExist_thenNoUpdateIsPerformed() {
    String studentCourseId = "1001";
    when(repository.searchByStudentCourseId(studentCourseId)).thenReturn(null);

    sut.promoteToFormalEnrollment(studentCourseId);

    verify(repository,never()).updateStatus(any());
  }
}