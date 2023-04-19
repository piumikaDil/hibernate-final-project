package hostel.dao.custom;

import hostel.dao.CrudDAO;
import hostel.entity.Reservation;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ReservationDAO extends CrudDAO<Reservation, String> {
    String generateNewID() throws SQLException, ClassNotFoundException;

    ArrayList<String> getAvailableRooms(String id) throws SQLException, ClassNotFoundException;

    ArrayList<String> getReservedRooms(String roomId) throws SQLException, ClassNotFoundException;
}
