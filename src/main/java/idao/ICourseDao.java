package idao;

import models.Course;

import java.util.List;

public interface ICourseDao {
    List<Course> listCoursesByTeacherLastName(String lastName);
}