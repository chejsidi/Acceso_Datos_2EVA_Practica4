package idao;

import models.Student;

import java.util.List;

public interface IStudentDao {
    /** Estudiantes apuntados al curso con ese id. */
    List<Student> listStudentsByCourseId(Integer courseId);
}