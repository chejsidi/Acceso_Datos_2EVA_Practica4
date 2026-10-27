package dao;

import idao.ICourseDao;
import models.Course;

import java.util.List;

public class CourseDao extends Dao<Course, Integer> implements ICourseDao {

    @Override
    public Course find(Integer id) {
        Course course = em.find(Course.class, id);
        return course;
    }

    @Override
    public List<Course> listCoursesByTeacherLastName(String lastName) {
        // c.teacher.lastName navega por la relación ManyToOne; Hibernate genera el JOIN solo
        String jpql = "SELECT c FROM Course c WHERE c.teacher.lastName = :lastName";
        return em.createQuery(jpql, Course.class)
                .setParameter("lastName", lastName)
                .getResultList();
    }
}