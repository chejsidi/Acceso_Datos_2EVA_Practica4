package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    private String title;

    // Un curso pertenece a un único profesor.
    // optional = false: un curso no puede guardarse sin profesor.
    // EAGER: al leer un curso se trae también su profesor.
    // cascade PERSIST: al guardar un curso nuevo, se guarda también su profesor si es nuevo.
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    // Relación inversa del OneToOne: la FK está en CourseMaterial
    // Es opcional (por defecto): un curso puede no tener material
    @OneToOne(mappedBy = "course")
    private CourseMaterial material;

    // ManyToMany con Student a través de la tabla intermedia students_courses.
    // Course es el lado propietario: los cambios en esta lista son los que se guardan en la tabla.
    // cascade PERSIST y MERGE: al guardar o actualizar el curso se guardan también los estudiantes nuevos.
    // Se inicializa vacía para que addStudent no dé NullPointerException en un curso nuevo.
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "students_courses",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id")
    )
    private List<Student> students = new ArrayList<>();

    public Course() {
    }

    public Course(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public CourseMaterial getMaterial() {
        return material;
    }

    public void setMaterial(CourseMaterial material) {
        this.material = material;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /** Añade el estudiante en los dos lados de la relación. */
    public void addStudent(Student student) {
        this.students.add(student);
        student.getCourses().add(this);
    }

    // Sin el material ni los estudiantes: imprimen su curso y se llamarían el uno al otro sin fin
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", teacher=" + teacher +
                '}';
    }
}