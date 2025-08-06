package raisetech.Student.management.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.Student.management.data.Student;
import raisetech.Student.management.data.StudentsCourses;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<StudentsCourses> studentsCourses;

}
