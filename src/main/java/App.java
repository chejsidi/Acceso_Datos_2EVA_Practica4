import dao.CourseDao;
import dao.CourseMaterialDao;
import dao.StudentDao;
import dao.TeacherDao;
import models.Course;
import models.CourseMaterial;
import models.Student;
import models.Teacher;

import java.time.LocalDate;

public class App {
    private static final TeacherDao teacherDao = new TeacherDao();
    private static final CourseDao courseDao = new CourseDao();
    private static final CourseMaterialDao courseMaterialDao = new CourseMaterialDao();
    private static final StudentDao studentDao = new StudentDao();

    private static Integer idCursoNuevo;  // curso creado en la Tarea 6, se usa también en la 7

    public static void main(String[] args) {
        tarea1();
        tarea2();
        tarea3();
        tarea4();
        tarea5();
        tarea6();
        tarea7();
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

        // b) guardar antes el profesor y después el curso
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

    private static void tarea6() {
        System.out.println("---------------------- TAREA 6 --------------------");
        courseDao.getEntityManager().clear();
        Course curso1 = courseDao.find(1);
        if (curso1 == null) {
            System.out.println("No existe el curso 1");
            return;
        }

        // a) Curso nuevo con varios estudiantes nuevos: la cascada PERSIST guarda también los estudiantes
        Course bbdd = new Course("Bases de Datos");
        bbdd.setTeacher(curso1.getTeacher());
        bbdd.addStudent(new Student("Ane", "Goikoetxea", LocalDate.of(2004, 5, 12), true));
        bbdd.addStudent(new Student("Jon", "Ibarra", LocalDate.of(2003, 11, 2), false));
        courseDao.create(bbdd);
        idCursoNuevo = bbdd.getId();
        System.out.println("a) Curso nuevo guardado con 2 estudiantes: " + bbdd);

        // b) Curso existente al que se añade un estudiante. El estudiante se guarda antes con su DAO;
        // al actualizar el curso, la cascada MERGE lo une al curso y se crea la fila en students_courses
        Student leire = new Student("Leire", "Mendizabal", LocalDate.of(2005, 1, 20), true);
        studentDao.create(leire);
        curso1.addStudent(leire);
        courseDao.update(curso1);
        System.out.println("b) " + leire.getFirstName() + " añadida al curso 1");

        // find del curso nuevo: la consulta no trae a los estudiantes (ManyToMany es LAZY por defecto).
        // Llegan con una segunda consulta, al llamar a getStudents()
        courseDao.getEntityManager().clear();
        Course leido = courseDao.find(idCursoNuevo);
        System.out.println("Curso leído: " + leido);
        System.out.println("Sus estudiantes: " + leido.getStudents());
    }

    private static void tarea7() {
        System.out.println("---------------------- TAREA 7 --------------------");
        if (idCursoNuevo == null) {
            System.out.println("Hace falta el curso de la Tarea 6");
            return;
        }
        // Estudiante nuevo apuntado a dos cursos que ya existen
        Student mikel = new Student("Mikel", "Zubiaurre", LocalDate.of(2004, 8, 30), false);
        studentDao.create(mikel);
        Course curso1 = courseDao.find(1);
        Course cursoNuevo = courseDao.find(idCursoNuevo);
        // Las filas de students_courses las guarda Course, que es el lado propietario de la relación:
        // por eso, aunque se apunte desde el estudiante, hay que actualizar los cursos.
        // Se apunta y se actualiza curso a curso: al hacer commit, Hibernate guarda todos los cursos
        // que tiene en memoria, y si el otro ya tuviera a Mikel en su lista daría "Detached entity passed to persist"
        mikel.addCourse(curso1);
        courseDao.update(curso1);
        mikel.addCourse(cursoNuevo);
        courseDao.update(cursoNuevo);
        System.out.println("Estudiante guardado con id " + mikel.getId() + " y apuntado a 2 cursos");

        // Consulta del estudiante: clear() para que lo lea de la BBDD y no de memoria.
        // Sus cursos llegan con otra consulta al llamar a getCourses() (LAZY)
        studentDao.getEntityManager().clear();
        Student leido = studentDao.find(mikel.getId());
        System.out.println("Estudiante leído: " + leido);
        for (Course c : leido.getCourses()) {
            System.out.println("  - " + c.getTitle());
        }
    }
}