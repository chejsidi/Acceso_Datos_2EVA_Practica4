package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    // Relación inversa: la FK está en Course (atributo "teacher"). LAZY: los cursos solo se cargan al pedirlos
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Course> courses;

    // JPA necesita un constructor vacío para crear los objetos al leerlos de la BBDD
    public Teacher() {
    }

    public Teacher(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    // Sin la lista de cursos: al ser LAZY, imprimirla obligaría a cargarlos, y como Course
    // imprime su Teacher, se llamarían el uno al otro sin fin
    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}