package hostel.bo.custom.impl;

import hostel.bo.custom.ReservationBO;
import hostel.dao.DAOFactory;
import hostel.dao.custom.QueryDAO;
import hostel.dao.custom.ReservationDAO;
import hostel.dao.custom.RoomDAO;
import hostel.dao.custom.StudentDAO;
import hostel.dto.ReservationDTO;
import hostel.dto.RoomDTO;
import hostel.dto.StudentDTO;
import hostel.entity.Reservation;
import hostel.entity.Room;
import hostel.entity.Student;

import java.sql.SQLException;
import java.util.ArrayList;

public class ReservationBOImpl implements ReservationBO {

    private final StudentDAO studentDAO = (StudentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.STUDENT);
    private final RoomDAO roomDAO = (RoomDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ROOM);
    private final ReservationDAO reservationDAO = (ReservationDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.RESERVATION);
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY_DAO);

    @Override
    public boolean saveReservation(ReservationDTO dto) throws SQLException, ClassNotFoundException {
        for (ReservationDTO reservationDTO : dto.getReservationDTOS()) {
            boolean save = reservationDAO.save(new Reservation(reservationDTO.getRes_id(), reservationDTO.getDate(), new Student(reservationDTO.getStudent_id()), new Room(reservationDTO.getRoom_type_id()), reservationDTO.getStatus()));
            if (!save) {
                return false;
            }
        }
        return true;
    }

    @Override
    public StudentDTO searchStudent(String id) throws SQLException, ClassNotFoundException {
        Student entity = studentDAO.search(id);
        return new StudentDTO(entity.getStudent_id(), entity.getName(), entity.getAddress(), entity.getContact_no(), entity.getDob(), entity.getGender());
    }

    @Override
    public RoomDTO searchRoom(String id) throws SQLException, ClassNotFoundException {
        Room entity = roomDAO.search(id);
        return new RoomDTO(entity.getRoom_type_id(), entity.getType(), entity.getKey_money(), entity.getQty());
    }

    @Override
    public boolean checkRoomIsAvailable(String id) throws SQLException, ClassNotFoundException {
        return roomDAO.exist(id);
    }

    @Override
    public boolean checkStudentIsAvailable(String id) throws SQLException, ClassNotFoundException {
        return studentDAO.exist(id);
    }

    @Override
    public String generateNewResID() throws SQLException, ClassNotFoundException {
        return reservationDAO.generateNewID();
    }

    @Override
    public ArrayList<StudentDTO> getAllStudents() throws SQLException, ClassNotFoundException {
        ArrayList<Student> all = studentDAO.getAll();
        ArrayList<StudentDTO> allStudents = new ArrayList<>();
        for (Student student : all) {
            allStudents.add(new StudentDTO(student.getStudent_id(), student.getName(), student.getAddress(), student.getContact_no(), student.getDob(), student.getGender()));
        }
        return allStudents;
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
    public ArrayList<ReservationDTO> getAllReservations() throws SQLException, ClassNotFoundException {
        ArrayList<Reservation> all = reservationDAO.getAll();
        ArrayList<ReservationDTO> allReservations = new ArrayList<>();
        for (Reservation res : all) {
            allReservations.add(new ReservationDTO(res.getRes_id(), res.getDate(), res.getStudent().getStudent_id(), res.getRoom().getRoom_type_id(), res.getStatus()));
        }
        return allReservations;
    }

    @Override
    public boolean updateReservation(ReservationDTO dto) throws SQLException, ClassNotFoundException {
        return reservationDAO.update(new Reservation(dto.getRes_id(), dto.getDate(), new Student(dto.getStudent_id()), new Room(dto.getRoom_type_id()), dto.getStatus()));
    }

    @Override
    public boolean deleteReservation(String id) throws SQLException, ClassNotFoundException {
        return reservationDAO.delete(id);
    }

    @Override
    public ArrayList<Object[]> searchReservation(String search) throws SQLException, ClassNotFoundException {
        return queryDAO.search(search);
    }
}
