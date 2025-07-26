package raisetech.Student.management;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchById(String id);

  @Insert("""
      INSERT INTO students (id, name, furigana, nickname, mail_address, municipalities, age, gender)
      VALUES(#{id}, #{name}, #{furigana}, #{nickname}, #{mailAddress}, #{municipalities}, #{age}, #{gender})
  """)
  void registerStudent(Student student);

  @Update("""
      UPDATE students SET name=#{name}, furigana=#{furigana}, nickname=#{nickname},
      mail_address=#{mailAddress}, municipalities=#{municipalities}, age=#{age}, gender=#{gender}
      WHERE id = #{id}
  """)
  void updateStudent(Student student);

  @Delete("DELETE FROM students WHERE id = #{id}")
  void deleteStudent(String id);
}
