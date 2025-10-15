package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "申込状況")
@Getter
@Setter
public class EnrollmentStatus {

  @Schema(description = "申込状況ID (登録時は自動採番)", example = "1")
  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください")
  private String id;

  @Schema(description = "申込状況に紐づく受講生コースID", example = "1")
  @NotBlank
  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください")
  private String studentCourseId;

  @Schema(description = "申込状況", allowableValues = {"仮申込", "本申込", "受講中", "受講終了"}, example = "仮申込")
  @NotBlank
  private String status;
}
