package raisetech.student.management.exception;

/**
 * 対象のリソースが見つからない場合にスローされる例外です。
 */
public class NotFoundException extends Exception {

  public NotFoundException(String message) {
    super(message);
  }
}
