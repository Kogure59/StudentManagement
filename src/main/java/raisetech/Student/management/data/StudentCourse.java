package raisetech.Student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  @Schema(description = "受講生コースID (登録時は自動採番)")
  @Pattern(regexp = "^\\d+$")
  private String id;

  @Schema(description = "コースに紐づく受講生ID")
  @Pattern(regexp = "^\\d+$")
  private String studentId;

  @Schema(description = "受講コース名", example = "Javaコース")
  @NotBlank
  private String courseName;

  @Schema(description = "受講開始日 (ISO 8601形式: yyyy-MM-ddTHH:mm:ss)", example = "2025-09-13T10:00:00")
  private LocalDateTime courseStartAt;

  @Schema(description = "受講終了日 (ISO 8601形式: yyyy-MM-ddTHH:mm:ss)", example = "2026-09-13T10:00:00")
  private LocalDateTime courseEndAt;
}
