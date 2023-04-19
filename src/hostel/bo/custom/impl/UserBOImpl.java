package hostel.bo.custom.impl;

import hostel.bo.custom.UserBO;
import hostel.dao.DAOFactory;
import hostel.dao.custom.ReservationDAO;
import hostel.dao.custom.RoomDAO;
import hostel.dao.custom.UserDAO;
import hostel.dto.RoomDTO;
import hostel.dto.UserDTO;
import hostel.entity.Room;
import hostel.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserBOImpl implements UserBO {
    private final UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.USER);
    private final RoomDAO roomDAO = (RoomDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ROOM);
    private final ReservationDAO reservationDAO = (ReservationDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.RESERVATION);

    @Override
    public RoomDTO searchRoom(String id) throws SQLException, ClassNotFoundException {
        Room entity = roomDAO.search(id);
        return new RoomDTO(entity.getRoom_type_id(), entity.getType(), entity.getKey_money(), entity.getQty());
    }

    @Override
    public UserDTO searchUser(String id) throws SQLException, ClassNotFoundException {
        User entity = userDAO.search(id);
        return new UserDTO(entity.getUser_id(), entity.getUsername(), entity.getPassword());
    }

    @Override
    public boolean checkRoomIsAvailable(String id) throws SQLException, ClassNotFoundException {
        return roomDAO.exist(id);
    }

    @Override
    public ArrayList<RoomDTO> getAllRooms() throws SQLException, ClassNotFoundException {
        ArrayList<Room> all = roomDAO.getAll();
        ArrayList<RoomDTO> allRooms = new ArrayList<>();
        for (Room room : all) {
            allRooms.add(new RoomDTO(room.getRoom_type_id(), room.getType(), room.getKey_money(), room.getQty()));
        }
        return allRooms;
    }

    @Override
    public ArrayList<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException {
        ArrayList<User> all = userDAO.getAll();
        ArrayList<UserDTO> allUsers = new ArrayList<>();
        for (User user : all) {
            allUsers.add(new UserDTO(user.getUser_id(), user.getUsername(), user.getPassword()));
        }
        return allUsers;
    }

    @Override
    public String generateNewUserID() throws SQLException, ClassNotFoundException {
        return userDAO.generateNewID();
    }

    @Override
    public UserDTO verifyUser(String id) throws SQLException, ClassNotFoundException {
        User user = userDAO.verifyUser(id);
        return new UserDTO(user.getUser_id(), user.getUsername(), user.getPassword());
    }

    @Override
    public boolean updateUser(UserDTO dto) throws SQLException, ClassNotFoundException {
        return userDAO.update(new User(dto.getUser_id(), dto.getUsername(), dto.getPassword()));
    }

    @Override
    public boolean saveUser(UserDTO dto) throws SQLException, ClassNotFoundException {
        return userDAO.save(new User(dto.getUser_id(), dto.getUsername(), dto.getPassword()));
    }

    @Override
    public boolean deleteUser(String id) throws SQLException, ClassNotFoundException {
        return userDAO.delete(id);
    }

    @Override
    public int availableRooms(String roomId) throws SQLException, ClassNotFoundException {
        ArrayList<String> all = reservationDAO.getAvailableRooms(roomId);
        return all.size();
    }

    @Override
    public int totalReservedRooms(String roomId) throws SQLException, ClassNotFoundException {
        ArrayList<String> all = reservationDAO.getReservedRooms(roomId);
        return all.size();
    }
}
