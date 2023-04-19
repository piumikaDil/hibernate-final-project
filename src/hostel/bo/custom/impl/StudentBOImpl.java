package hostel.bo.custom.impl;

import hostel.bo.custom.StudentBO;
import hostel.dao.DAOFactory;
import hostel.dao.custom.StudentDAO;
import hostel.dto.StudentDTO;
import hostel.entity.Student;

import java.sql.SQLException;
import java.util.ArrayList;

public class StudentBOImpl implements StudentBO {

    private final StudentDAO studentDAO = (StudentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.STUDENT);

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
    public boolean saveStudent(StudentDTO dto) throws SQLException, ClassNotFoundException {
        return studentDAO.save(new Student(dto.getStudent_id(), dto.getName(), dto.getAddress(), dto.getContact_no(), dto.getDob(), dto.getGender()));
    }

    @Override
    public boolean updateStudent(StudentDTO dto) throws SQLException, ClassNotFoundException {
        return studentDAO.update(new Student(dto.getStudent_id(), dto.getName(), dto.getAddress(), dto.getContact_no(), dto.getDob(), dto.getGender()));
    }

    @Override
    public boolean studentExist(String id) throws SQLException, ClassNotFoundException {
        return studentDAO.exist(id);
    }

    @Override
    public boolean deleteStudent(String id) throws SQLException, ClassNotFoundException {
        return studentDAO.delete(id);
    }

    @Override
    public StudentDTO searchStudent(String id) throws SQLException, ClassNotFoundException {
        Student entity = studentDAO.search(id);
        if (entity != null) {
            return new StudentDTO(entity.getStudent_id(), entity.getName(), entity.getAddress(), entity.getContact_no(), entity.getDob(), entity.getGender());
        } else {
            return null;
        }
    }
}
