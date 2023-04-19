package hostel.bo.custom;

import hostel.bo.SuperBO;
import hostel.dto.StudentDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface StudentBO extends SuperBO {
    ArrayList<StudentDTO> getAllStudents() throws SQLException, ClassNotFoundException;

    boolean saveStudent(StudentDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateStudent(StudentDTO dto) throws SQLException, ClassNotFoundException;

    boolean studentExist(String id) throws SQLException, ClassNotFoundException;

    StudentDTO searchStudent(String id) throws SQLException, ClassNotFoundException;

    boolean deleteStudent(String id) throws SQLException, ClassNotFoundException;
}
