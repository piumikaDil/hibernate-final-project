package hostel.dao.custom.impl;

import hostel.dao.custom.RoomDAO;
import hostel.entity.Room;
import hostel.util.FactoryConfiguration;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    @Override
    public ArrayList<Room> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        ArrayList<Room> rooms = new ArrayList<>();

        try {
            transaction = session.beginTransaction();
            List<Room> roomList = session.createQuery("FROM Room").list();
            rooms.addAll(roomList);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rooms;
    }

    @Override
    public boolean save(Room entity) throws SQLException, ClassNotFoundException {
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
    public boolean update(Room entity) throws SQLException, ClassNotFoundException {
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
    public Room search(String id) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        Room room = null;

        try {
            transaction = session.beginTransaction();
            room = session.get(Room.class, id);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return room;
    }

    @Override
    public boolean exist(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean b = false;

        try {
            transaction = session.beginTransaction();
            if (session.get(Room.class, id) != null) b = true;
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
            Room room = session.load(Room.class, id);
            session.delete(room);
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
