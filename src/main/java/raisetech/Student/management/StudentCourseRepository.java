package raisetech.Student.management;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentCourseRepository {

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchCoursesByStudentId(String studentId);

  @Insert("""
      INSERT INTO students_courses (id, student_id, course_name, course_start_date, course_end_date)
      VALUES (#{id}, #{studentId}, #{courseName}, #{courseStartDate}, #{courseEndDate})
  """)
  void registerCourse(StudentCourse course);

  @Update("""
      UPDATE students_courses SET course_name=#{courseName},
      course_start_date=#{courseStartDate}, course_end_date=#{courseEndDate}
      WHERE id = #{id}
  """)
  void updateCourse(StudentCourse course);

  @Delete("DELETE FROM students_courses WHERE id = #{id}")
  void deleteCourse(String id);
}
