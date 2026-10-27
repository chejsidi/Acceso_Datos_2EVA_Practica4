package dao;

import idao.IStudentDao;
import models.Student;

import java.util.List;

public class StudentDao extends Dao<Student, Integer> implements IStudentDao {

    @Override
    public Student find(Integer id) {
        Student student = em.find(Student.class, id);
        return student;
    }

    @Override
    public List<Student> listStudentsByCourseId(Integer courseId) {
        // JOIN s.courses recorre la relación ManyToMany: Hibernate pasa por students_courses él solo
        String jpql = "SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId";
        return em.createQuery(jpql, Student.class)
                .setParameter("courseId", courseId)
                .getResultList();
    }
}