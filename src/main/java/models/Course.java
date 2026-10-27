package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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

    // Relación inversa del OneToOne: la FK está en CourseMaterial.
    // Es opcional (por defecto): un curso puede no tener material
    @OneToOne(mappedBy = "course")
    private CourseMaterial material;

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

    // Sin el material: CourseMaterial ya imprime su curso y se llamarían el uno al otro sin fin
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", teacher=" + teacher +
                '}';
    }
}