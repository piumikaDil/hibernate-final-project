package hostel.bo.custom;

import hostel.bo.SuperBO;
import hostel.dto.RoomDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface RoomBO extends SuperBO {
    ArrayList<RoomDTO> getAllRooms() throws SQLException, ClassNotFoundException;

    boolean deleteRoom(String id) throws SQLException, ClassNotFoundException;

    boolean saveRoom(RoomDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateRoom(RoomDTO dto) throws SQLException, ClassNotFoundException;

    RoomDTO searchRoom(String id) throws SQLException, ClassNotFoundException;

    boolean roomExist(String id) throws SQLException, ClassNotFoundException;
}
