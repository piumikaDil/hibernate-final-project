package hostel.bo.custom;

import hostel.bo.SuperBO;
import hostel.dto.ReservationDTO;
import hostel.dto.RoomDTO;
import hostel.dto.StudentDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ReservationBO extends SuperBO {
    boolean saveReservation(ReservationDTO dto) throws SQLException, ClassNotFoundException;

    StudentDTO searchStudent(String id) throws SQLException, ClassNotFoundException;

    RoomDTO searchRoom(String id) throws SQLException, ClassNotFoundException;

    boolean checkRoomIsAvailable(String id) throws SQLException, ClassNotFoundException;

    boolean checkStudentIsAvailable(String id) throws SQLException, ClassNotFoundException;

    String generateNewResID() throws SQLException, ClassNotFoundException;

    ArrayList<StudentDTO> getAllStudents() throws SQLException, ClassNotFoundException;

    ArrayList<RoomDTO> getAllRooms() throws SQLException, ClassNotFoundException;

    ArrayList<ReservationDTO> getAllReservations() throws SQLException, ClassNotFoundException;

    boolean updateReservation(ReservationDTO reservationDTO) throws SQLException, ClassNotFoundException;

    boolean deleteReservation(String id) throws SQLException, ClassNotFoundException;

    ArrayList<Object[]> searchReservation(String id) throws SQLException, ClassNotFoundException;
}
