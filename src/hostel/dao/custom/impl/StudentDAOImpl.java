package hostel.dao.custom.impl;

import hostel.dao.custom.StudentDAO;
import hostel.entity.Student;
import hostel.util.FactoryConfiguration;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public ArrayList<Student> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        ArrayList<Student> students = new ArrayList<>();

        try {
            transaction = session.beginTransaction();
            List<Student> studentList = session.createQuery("FROM Student").list();
            students.addAll(studentList);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return students;
    }

    @Override
    public boolean save(Student entity) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean b = false;
        try {
            transaction = session.beginTransaction();
            session.save(entity);
            b = true;
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return b;
    }

    @Override
    public boolean update(Student entity) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean b = false;

        try {
            transaction = session.beginTransaction();
            session.update(entity);
            b = true;
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return b;
    }

    @Override
    public Student search(String id) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        Student student = null;

        try {
            transaction = session.beginTransaction();
            student = session.get(Student.class, id);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return student;
    }

    @Override
    public boolean exist(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean b = false;

        try {
            transaction = session.beginTransaction();
            if (session.get(Student.class, id) != null) b = true;
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return b;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean b = false;

        try {
            transaction = session.beginTransaction();
            Student student = session.load(Student.class, id);
            session.delete(student);
            b = true;
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return b;
    }
}
