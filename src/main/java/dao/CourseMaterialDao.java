package dao;

import idao.ICourseMaterialDao;
import models.CourseMaterial;

public class CourseMaterialDao extends Dao<CourseMaterial, Integer> implements ICourseMaterialDao {

    @Override
    public CourseMaterial find(Integer id) {
        CourseMaterial courseMaterial = em.find(CourseMaterial.class, id);
        return courseMaterial;
    }
}