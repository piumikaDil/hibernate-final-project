package hostel.dao.custom;

import hostel.dao.SuperDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {
    ArrayList<Object[]> search(String search) throws SQLException, ClassNotFoundException;
}
