package hostel.bo.custom.impl;

import hostel.bo.custom.RoomBO;
import hostel.dao.DAOFactory;
import hostel.dao.custom.RoomDAO;
import hostel.dto.RoomDTO;
import hostel.entity.Room;

import java.sql.SQLException;
import java.util.ArrayList;

public class RoomBOImpl implements RoomBO {

    private final RoomDAO roomDAO = (RoomDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ROOM);

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
    public boolean saveRoom(RoomDTO dto) throws SQLException, ClassNotFoundException {
        return roomDAO.save(new Room(dto.getRoom_type_id(), dto.getType(), dto.getKey_money(), dto.getQty()));
    }

    @Override
    public boolean updateRoom(RoomDTO dto) throws SQLException, ClassNotFoundException {
        return roomDAO.update(new Room(dto.getRoom_type_id(), dto.getType(), dto.getKey_money(), dto.getQty()));
    }

    @Override
    public boolean roomExist(String id) throws SQLException, ClassNotFoundException {
        return roomDAO.exist(id);
    }

    @Override
    public boolean deleteRoom(String id) throws SQLException, ClassNotFoundException {
        return roomDAO.delete(id);
    }

    @Override
    public RoomDTO searchRoom(String id) throws SQLException, ClassNotFoundException {
        Room entity = roomDAO.search(id);
        if (entity != null) {
            return new RoomDTO(entity.getRoom_type_id(), entity.getType(), entity.getKey_money(), entity.getQty());
        } else {
            return null;
        }
    }
}
