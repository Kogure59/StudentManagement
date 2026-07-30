# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

When modifying features:
- Update `README.md` when user-facing behavior changes.
- Update API documentation when endpoints or request/response models change.
- Keep documentation synchronized with the implementation.

Prioritize:
- Readability
- Maintainability
- Consistency
over quick fixes or unnecessary optimizations.

## Commands

```bash
./gradlew build                # full build (compiles + runs tests)
./gradlew test                 # run all tests
./gradlew bootRun              # run the app locally (needs MySQL, see below)
./gradlew bootJar              # build the executable jar (used by CI/deploy)

# single test class
./gradlew test --tests "raisetech.student.management.service.StudentServiceTest"

# single test method
./gradlew test --tests "raisetech.student.management.service.StudentServiceTest.methodName"
```

Tests run against an in-memory H2 database (`MODE=MySQL`, schema/data loaded from
`src/test/resources/schema.sql` and `data.sql`) so `./gradlew test` needs no external DB.
`bootRun` connects to a real MySQL instance per `src/main/resources/application.properties`
(`jdbc:mysql://localhost:3306/StudentManagement`).

## Architecture

Standard layered architecture, split across two resource areas that share the same layers:
student management (`StudentController`) and enrollment status (`EnrollmentStatusController`).

```
Controller -> Service -> Repository (MyBatis @Mapper interface) -> mapper/*.xml (SQL)
```

- **Repository layer is MyBatis-based**: interfaces in `repository/` are annotated `@Mapper`
  with no SQL in Java; the actual queries live in the sibling XML files under
  `src/main/resources/mapper/`. `mybatis.configuration.map-underscore-to-camel-case=true`
  handles `snake_case` (DB) <-> `camelCase` (Java) conversion automatically. Search endpoints with
  optional filters (`searchStudentByCondition`, `searchStudentCourseByCondition`) build the WHERE
  clause dynamically with MyBatis `<if>` tags in the XML rather than in Java.
- **`StudentDetail` is the API-facing aggregate**: it wraps a `Student` plus its
  `List<StudentCourse>`. It is assembled/deassembled by `StudentConverter`, which joins students
  to their courses **in memory** (matching on `studentId`) rather than via a SQL join — both
  lists are fetched from the repository separately and merged in `StudentConverter.convertStudentDetails`.
- **Course/status conditions are applied post-hoc in `StudentService`**: when a course-related
  filter is present, the service fetches matching course rows separately, collects the student IDs
  they belong to, then intersects that set with the student-level filter results. An empty
  course-filter match short-circuits to an empty result even if the student-level filter would
  have matched something.
- **Deletion is logical, not physical**: `Student.isDeleted` is a flag column, toggled via the
  normal update endpoint — there is no delete endpoint/query.
- **Enrollment status has its own state machine-ish flow**: a `StudentCourse` gets an
  `EnrollmentStatus` created via a dedicated "initial" endpoint (defaults to 仮申込/provisional),
  which can later be promoted to 本申込/formal via `.../enrollmentStatus/formal`, independent of
  the general update endpoint.
- **API docs**: endpoints are annotated with springdoc-openapi (`@Operation`/`@Parameter`/`@Schema`)
  in Japanese — keep new endpoints consistent with this if adding to the public API surface.
- **Package name vs. directory casing differ**: source lives under
  `src/main/java/raisetech/Student/management/...` (capital `Student`) but the Java package
  declaration is lowercase `raisetech.student.management`. This only works because the
  filesystem is case-insensitive; be careful not to introduce a second, differently-cased
  directory when adding files — always match the existing package declaration casing (lowercase).

## CI/CD

`.github/workflows/JavaTest.yml` runs on push/PR to `main`: `./gradlew test` -> `./gradlew bootJar`
-> SCP the jar to an EC2 host -> SSH in and restart the `StudentManagement` systemd service. The
EC2/RDS environment this deploys to has since been torn down, so the deploy step is expected to
fail even though the workflow itself is functional.

## Project Purpose

This project was developed as part of the RaiseTech Java course.
The purpose is to demonstrate:
- Spring Boot
- MyBatis
- REST API
- Testing with JUnit5 / Mockito
- GitHub Actions
- AWS deployment

## Coding Guidelines

- Keep controller thin
- Business logic belongs in Service
- SQL belongs in MyBatis XML
- Add tests for new features
