CREATE TABLE IF NOT EXISTS students
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    kana_name VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(50) NOT NULL,
    area VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    remark TEXT,
    is_deleted boolean NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS students_courses
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_name VARCHAR(50) NOT NULL,
    course_start_at TIMESTAMP,
    course_end_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS enrollment_status
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_course_id INT NOT NULL,
    status VARCHAR(10) NOT NULL,
    FOREIGN KEY (student_course_id) REFERENCES students_courses(id)
);