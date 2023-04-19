package hostel.util;

import hostel.entity.Reservation;
import hostel.entity.Room;
import hostel.entity.Student;
import hostel.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;

    private FactoryConfiguration() {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemClassLoader().getResourceAsStream("/hostel/resources/hibernate.properties"));
        } catch (Exception ignored) {
        }

        Configuration configuration = new Configuration().mergeProperties(properties)
                .addAnnotatedClass(Student.class).addAnnotatedClass(Room.class)
                .addAnnotatedClass(Reservation.class).addAnnotatedClass(User.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    public static FactoryConfiguration getInstance() {
        return factoryConfiguration == null ? factoryConfiguration = new FactoryConfiguration() : factoryConfiguration;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
