import dao.CourseDao;
import dao.TeacherDao;
import models.Course;
import models.Teacher;

public class App {
    public static void main(String[] args) {
        TeacherDao teacherDao = new TeacherDao();
        CourseDao courseDao = new CourseDao();

        System.out.println("---------------------- TAREA 1 --------------------");
        // Tarea 1: curso con id 1, que trae también su profesor (ManyToOne, EAGER)
        Course course = courseDao.find(1);
        System.out.println("Curso 1: " + course);

        // Su profesor y la lista de sus cursos (OneToMany, LAZY: se cargan al llamar a getCourses)
        if (course != null) {
            Teacher teacher = teacherDao.find(course.getTeacher().getId());
            System.out.println("Profesor del curso: " + teacher);
            System.out.println("Sus cursos: " + teacher.getCourses());
        }
        System.out.println("---------------------- TAREA 2 --------------------");
        // Tarea 2: create, update y delete de Teacher
        Teacher nuevo = new Teacher("Eider", "Arbaiza");
        teacherDao.create(nuevo);
        System.out.println("Creado: " + teacherDao.find(nuevo.getId()));

        nuevo.setLastName("Arbaiza Ruiz");
        teacherDao.update(nuevo);
        System.out.println("Actualizado: " + teacherDao.find(nuevo.getId()));

        teacherDao.delete(nuevo);
        System.out.println("Después de borrar: " + teacherDao.find(nuevo.getId()));
    }
}