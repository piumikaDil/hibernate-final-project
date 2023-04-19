package hostel.bo.custom;

import hostel.bo.SuperBO;
import hostel.dto.RoomDTO;
import hostel.dto.UserDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserBO extends SuperBO {
    RoomDTO searchRoom(String s) throws SQLException, ClassNotFoundException;

    UserDTO searchUser(String s) throws SQLException, ClassNotFoundException;

    boolean checkRoomIsAvailable(String id) throws SQLException, ClassNotFoundException;

    ArrayList<RoomDTO> getAllRooms() throws SQLException, ClassNotFoundException;

    ArrayList<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException;

    String generateNewUserID() throws SQLException, ClassNotFoundException;

    UserDTO verifyUser(String id) throws SQLException, ClassNotFoundException;

    boolean updateUser(UserDTO userDTO) throws SQLException, ClassNotFoundException;

    boolean saveUser(UserDTO userDTO) throws SQLException, ClassNotFoundException;

    boolean deleteUser(String id) throws SQLException, ClassNotFoundException;

    int availableRooms(String roomId) throws SQLException, ClassNotFoundException;

    int totalReservedRooms(String roomId) throws SQLException, ClassNotFoundException;
}
