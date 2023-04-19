package hostel.dao.custom;

import hostel.dao.CrudDAO;
import hostel.entity.User;

import java.sql.SQLException;

public interface UserDAO extends CrudDAO<User, String> {
    String generateNewID() throws SQLException, ClassNotFoundException;

    User verifyUser(String id) throws SQLException, ClassNotFoundException;
}
