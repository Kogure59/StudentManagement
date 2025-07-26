package raisetech.Student.management;

import java.time.LocalDate;

public class StudentCourse {
  private String id;
  private String studentId;
  private String courseName;
  private LocalDate courseStartDate;
  private LocalDate courseEndDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public LocalDate getCourseStartDate() {
    return courseStartDate;
  }

  public void setCourseStartDate(LocalDate courseStartDate) {
    this.courseStartDate = courseStartDate;
  }

  public LocalDate getCourseEndDate() {
    return courseEndDate;
  }

  public void setCourseEndDate(LocalDate courseEndDate) {
    this.courseEndDate = courseEndDate;
  }
}
