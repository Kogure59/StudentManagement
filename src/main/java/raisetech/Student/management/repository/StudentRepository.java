package raisetech.Student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import raisetech.Student.management.data.Student;
import raisetech.Student.management.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentCourse();

  @Insert("INSERT INTO students (name, kana_name, nickname, email, area, age, gender, remark, is_deleted) "
      + "VALUES (#{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);
}
