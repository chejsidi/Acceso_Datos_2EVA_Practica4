package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private LocalDate birthDate;

    private boolean wantsNewsletter;

    // Relación inversa del ManyToMany: la tabla intermedia está definida en Course (atributo "students")
    @ManyToMany(mappedBy = "students")
    private List<Course> courses = new ArrayList<>();

    public Student() {
    }

    public Student(String firstName, String lastName, LocalDate birthDate, boolean wantsNewsletter) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.wantsNewsletter = wantsNewsletter;
    }

    /**
     * Apunta al estudiante en un curso. Se añade en los dos lados porque quien guarda la
     * relación en la tabla intermedia es Course: si solo se añadiera aquí, no se guardaría.
     */
    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isWantsNewsletter() {
        return wantsNewsletter;
    }

    public void setWantsNewsletter(boolean wantsNewsletter) {
        this.wantsNewsletter = wantsNewsletter;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    // Sin la lista de cursos, para no entrar en un bucle con el toString de Course
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", wantsNewsletter=" + wantsNewsletter +
                '}';
    }
}