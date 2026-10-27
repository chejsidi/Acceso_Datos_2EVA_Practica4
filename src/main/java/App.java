import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {
    public static void main(String[] args) {
        // Crear el EntityManagerFactory obliga a Hibernate a conectarse a la base de datos:
        // si no hay error, la configuración del persistence.xml es correcta
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        System.out.println("Conectado a la base de datos");
        emf.close();
    }
}