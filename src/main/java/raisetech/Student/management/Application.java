package raisetech.Student.management;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/students")  // ベース URL は /students
public class Application {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudentCourseRepository studentCourseRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// 指定された ID の受講生情報を取得する。
	// GET /students/{id}
	@GetMapping("/{id}")
	public Student getStudent(@PathVariable String id) {
		return studentRepository.searchById(id);
	}

	// 新しい受講生情報を登録する。
	// POST /students
	// JSON のリクエストボディを Student オブジェクトにマッピング。
	@PostMapping
	public void registerStudent(@RequestBody Student student) {
		studentRepository.registerStudent(student);
	}

	// 指定された ID の受講生情報を更新する。
	// PATCH /students/{id}
	// JSON のリクエストボディの内容で上書き。
	@PatchMapping("/{id}")
	public void updateStudent(@PathVariable String id, @RequestBody Student student) {
		student.setId(id);
		studentRepository.updateStudent(student);
	}

	// 指定された ID の受講生情報を削除する。
	// DELETE /students/{id}
	@DeleteMapping("/{id}")
	public void deleteStudent(@PathVariable String id) {
		studentRepository.deleteStudent(id);
	}

	// --- 受講生コース関連 ---

	// 指定された受講生が登録しているコース一覧を取得する。
	// GET /students/{id}/courses
	@GetMapping("/{id}/courses")
	public List<StudentCourse> getCourses(@PathVariable String id) {
		return studentCourseRepository.searchCoursesByStudentId(id);
	}

	// 指定された受講生に新しいコースを追加登録する。
	// POST /students/{id}/courses
	// JSON のリクエストボディを StudentCourse オブジェクトにマッピング。
	@PostMapping("/{id}/courses")
	public void registerCourse(@PathVariable String id, @RequestBody StudentCourse course) {
		course.setStudentId(id);
		studentCourseRepository.registerCourse(course);
	}

	// 指定されたコース情報を更新する。
	// PATCH /students/courses/{courseId}
	@PatchMapping("/courses/{courseId}")
	public void updateCourse(@PathVariable String courseId, @RequestBody StudentCourse course) {
		course.setId(courseId);
		studentCourseRepository.updateCourse(course);
	}

	// 指定されたコース情報を削除する。
	// DELETE /students/courses/{courseId}
	@DeleteMapping("/courses/{courseId}")
	public void deleteCourse(@PathVariable String courseId) {
		studentCourseRepository.deleteCourse(courseId);
	}
}
