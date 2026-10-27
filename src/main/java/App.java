import dao.CourseDao;
import dao.CourseMaterialDao;
import dao.TeacherDao;
import models.Course;
import models.CourseMaterial;
import models.Teacher;

public class App {
    private static final TeacherDao teacherDao = new TeacherDao();
    private static final CourseDao courseDao = new CourseDao();
    private static final CourseMaterialDao courseMaterialDao = new CourseMaterialDao();

    public static void main(String[] args) {
        tarea1();
        tarea2();
        tarea3();
        tarea4();
        tarea5();
    }

    private static void tarea1() {
        System.out.println("---------------------- TAREA 1 --------------------");
        // Curso con id 1, que trae también su profesor (ManyToOne, EAGER)
        Course course = courseDao.find(1);
        System.out.println("Curso 1: " + course);

        // Su profesor y la lista de sus cursos (OneToMany, LAZY: se cargan al llamar a getCourses)
        if (course != null) {
            Teacher teacher = teacherDao.find(course.getTeacher().getId());
            System.out.println("Profesor del curso: " + teacher);
            System.out.println("Sus cursos: " + teacher.getCourses());
        }
    }

    private static void tarea2() {
        System.out.println("---------------------- TAREA 2 --------------------");
        Teacher nuevo = new Teacher("Eider", "Arbaiza");
        teacherDao.create(nuevo);
        System.out.println("Creado: " + teacherDao.find(nuevo.getId()));

        nuevo.setLastName("Arbaiza Ruiz");
        teacherDao.update(nuevo);
        System.out.println("Actualizado: " + teacherDao.find(nuevo.getId()));

        teacherDao.delete(nuevo);
        System.out.println("Después de borrar: " + teacherDao.find(nuevo.getId()));  // null
    }

    private static void tarea3() {
        System.out.println("---------------------- TAREA 3 --------------------");
        // Curso sin profesor: la relación es obligatoria (optional = false), así que debe fallar
        try {
            courseDao.create(new Course("Curso sin profesor"));
            System.out.println("Se ha guardado (no debería)");
        } catch (Exception e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
    }

    private static void tarea4() {
        System.out.println("---------------------- TAREA 4 --------------------");
        // a) Curso nuevo con un profesor nuevo que todavía no está guardado
        try {
            Teacher eider = new Teacher("Eider", "Arbaiza");
            Course psp = new Course("Programación de Servicios y Procesos");
            psp.setTeacher(eider);
            courseDao.create(psp);
            System.out.println("a) Guardado con su profesor: " + courseDao.find(psp.getId()));
        } catch (Exception e) {
            System.out.println("a) Error: " + e.getMessage());
        }

        // b) Solución del PDF sin cascada: guardar antes el profesor y después el curso
        try {
            Teacher mikel = new Teacher("Mikel", "Etxebarria");
            teacherDao.create(mikel);
            Course di = new Course("Desarrollo de Interfaces");
            di.setTeacher(mikel);
            courseDao.create(di);
            System.out.println("b) Guardado con su profesor: " + courseDao.find(di.getId()));
        } catch (Exception e) {
            System.out.println("b) Error: " + e.getMessage());
        }
    }

    private static void tarea5() {
        System.out.println("---------------------- TAREA 5 --------------------");
        // clear(): el EntityManager olvida los objetos que ya tenía en memoria, así find vuelve a leer de la BBDD
        courseDao.getEntityManager().clear();
        Course course = courseDao.find(1);
        if (course == null) {
            System.out.println("No existe el curso 1");
            return;
        }

        // Cada curso solo puede tener un material: si ya lo tiene (otra ejecución), no se crea otro
        CourseMaterial material = course.getMaterial();
        if (material == null) {
            material = new CourseMaterial("https://ejemplo.com/acceso-a-datos");
            material.setCourse(course);
            courseMaterialDao.create(material);
            System.out.println("Material creado: " + material);
        } else {
            System.out.println("El curso 1 ya tenía material: " + material);
        }

        // find del material: trae también su curso (y el profesor del curso)
        courseMaterialDao.getEntityManager().clear();
        System.out.println("Material leído: " + courseMaterialDao.find(material.getId()));

        // find del curso: si la consulta trae también el material, el OneToOne es EAGER por defecto
        courseDao.getEntityManager().clear();
        Course conMaterial = courseDao.find(1);
        System.out.println("Curso leído: " + conMaterial);
        System.out.println("Su material: " + conMaterial.getMaterial().getUrl());
    }
}