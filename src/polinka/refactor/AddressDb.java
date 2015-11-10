package polinka.refactor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddressDb {
    private Executor executor = Executors.newFixedThreadPool(18);
    private final int CACHE_SIZE = 50;

    public AddressDb() {
        try {
            Class.forName("oracle.jdbc.ThinDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//
//		new Thread().start(); // It will do nothin' :(
    }

    public void addPerson(Person person) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@prod", "admin", "beefhead");
             PreparedStatement statement = connection.prepareStatement("insert into AddressEntry values (?, ?, ?)")) {
            statement.setLong(1, System.currentTimeMillis());
            statement.setString(2, person.getName());
            statement.setString(3, person.getPhoneNumber().getNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Looks for the person by name.
     *
     * @return first person found with given name or null if found nothing.
     */
    public Person findPerson(String name) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@prod", "admin", "beefhead");
             PreparedStatement statement = connection.
                     prepareStatement("select * from AddressEntry where name = " + name);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return new Person(resultSet.getString("name"), new PhoneNumber(resultSet.getString("phoneNumber")));
            } else return null;
        } catch (SQLException e) {
            return null;
        }

    }

    public List<Person> getAll() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@prod", "admin", "beefhead");
             PreparedStatement statement = connection.prepareStatement("select * from AddressEntry");
             ResultSet resultSet = statement.executeQuery()) {
            List<Person> entries = new LinkedList<>();
            while (resultSet.next()) {
                entries.add(new Person(resultSet.getString("name"),new PhoneNumber(resultSet.getString("phoneNumber"))));
            }
            return entries;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
