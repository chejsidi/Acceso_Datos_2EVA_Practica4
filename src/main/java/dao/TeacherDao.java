package dao;

import idao.ITeacherDao;
import models.Teacher;

public class TeacherDao extends Dao<Teacher, Integer> implements ITeacherDao {

    @Override
    public Teacher find(Integer id) {
        Teacher teacher = em.find(Teacher.class, id);
        return teacher;
    }
}