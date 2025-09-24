package raisetech.Student.management.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  @Schema(description = "受講生ID (登録時は自動採番)", example = "1")
  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください")
  private String id;

  @Schema(description = "氏名", example = "山田太郎")
  @NotBlank
  private String name;

  @Schema(description = "カナ名", example = "ヤマダタロウ")
  @NotBlank
  private String kanaName;

  @Schema(description = "ニックネーム", example = "たろちゃん")
  @NotBlank
  private String nickname;

  @Schema(description = "メールアドレス", example = "taro@example.com")
  @NotBlank
  @Email
  private String email;

  @Schema(description = "地域", example = "東京都")
  @NotBlank
  private String area;

  @Schema(description = "年齢", example = "25")
  private int age;

  @Schema(description = "性別", example = "男性")
  @NotBlank
  private String gender;

  @Schema(description = "備考")
  private String remark;

  @Schema(description = "論理削除フラグ (true = 削除済、 false = 登録中)")
  @JsonProperty("isDeleted")
  private boolean isDeleted;
}
