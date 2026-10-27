# Práctica 4.01 - Jakarta EE, Hibernate y relaciones

Proyecto Jakarta EE que usa Hibernate (implementación de JPA) para mapear entidades Java a una base de datos MariaDB, con los tres tipos de relación: one-to-many / many-to-one, one-to-one y many-to-many.

## Requisitos

- Docker
- JDK 17 o superior
- IntelliJ IDEA

Dependencias Maven:

| Librería | Uso |
|---|---|
| `org.hibernate.orm:hibernate-core` | ORM (implementación de JPA) |
| `org.mariadb.jdbc:mariadb-java-client` | Driver de MariaDB |
| `org.hibernate.validator:hibernate-validator` y `org.glassfish.expressly:expressly` | Validación de `@NotNull` |

## Puesta en marcha

1. Arrancar MariaDB desde la carpeta del proyecto. Se crea la base de datos `AD_aulavirtual`:

   ```bash
   docker compose up -d
   ```

2. Abrir el proyecto en IntelliJ y ejecutar `App` una vez. Hibernate crea las tablas a partir de las entidades (`hibernate.hbm2ddl.auto = update`).

3. Insertar un profesor y un curso, que usan las Tareas 1, 5 y 8:

   ```bash
   docker exec -i mariadb-aulavirtual mariadb -uroot -proot AD_aulavirtual -e "INSERT INTO Teacher (firstName, lastName) VALUES ('Josu', 'López'); INSERT INTO Course (title, teacher_id) VALUES ('Acceso a Datos', LAST_INSERT_ID());"
   ```

4. Ejecutar `App` de nuevo. Cada tarea muestra su resultado y, gracias a `hibernate.show_sql = true`, las consultas SQL que genera Hibernate.

Para empezar de cero: `docker compose down -v` y repetir los pasos.

La conexión se configura en `src/main/resources/META-INF/persistence.xml`: base de datos `AD_aulavirtual` en `localhost:3306`, usuario `root`, contraseña `root`.

## Estructura

```
src/main/java
├── App.java      Pruebas de las tareas (un método por tarea)
├── models        Entidades: Teacher, Course, CourseMaterial, Student
├── idao          Interfaces de los DAO
└── dao           Dao (clase abstracta con el CRUD) y un DAO por entidad
```

## Relaciones

| Relación | Entidades | Dónde está la FK | Carga por defecto |
|---|---|---|---|
| Many-to-one / one-to-many | `Course` → `Teacher` | `Course.teacher_id` | `EAGER` (curso → profesor), `LAZY` (profesor → cursos) |
| One-to-one | `CourseMaterial` → `Course` | `CourseMaterial.course_id` | `EAGER` |
| Many-to-many | `Course` ↔ `Student` | Tabla intermedia `students_courses` | `LAZY` |

## Tareas

| Tarea | Qué se prueba | Resultado |
|---|---|---|
| 1 | `find` de un curso | Trae también su profesor (`EAGER`) |
| 2 | `create`, `update` y `delete` de `Teacher` | Funcionan |
| 3 | Guardar un curso sin profesor | Error: la relación es obligatoria (`optional = false`) |
| 4 | Guardar un curso con un profesor nuevo | Sin cascada da error; con `cascade = PERSIST` se guardan los dos |
| 5 | Crear un `CourseMaterial` y buscar el curso | El one-to-one es `EAGER` |
| 6 | Curso nuevo con estudiantes y curso existente con un estudiante más | Funciona con `cascade = {PERSIST, MERGE}` y la lista inicializada; el many-to-many es `LAZY` |
| 7 | Relación inversa: estudiante apuntado a varios cursos | Se guarda a través de `Course`, el lado propietario |
| 8 | `listCoursesByTeacherLastName` | Consulta JPQL por apellido del profesor |
| 9 | `listStudentsByCourseId` | Consulta JPQL por id de curso |

## Notas

- Cada DAO tiene su propio `EntityManager`. Un objeto guardado con un DAO es *detached* para los demás, y la cascada `PERSIST` sobre él da "Detached entity passed to persist". Por eso en la Tarea 7 se apunta al estudiante en cada curso justo antes de actualizarlo.
- Si una operación del `Dao` falla, se hace `rollback()` y `clear()`, para que la transacción no quede abierta y el programa pueda seguir.
- Los `toString` no imprimen las listas de la relación inversa, para evitar que dos entidades se llamen la una a la otra sin fin.
- La lista de estudiantes es una `List`: al añadir uno, Hibernate borra y vuelve a insertar las filas del curso en `students_courses`. Con un `Set` insertaría solo la nueva.