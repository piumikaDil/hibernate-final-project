package hostel.dao.custom.impl;

import hostel.dao.custom.ReservationDAO;
import hostel.entity.Reservation;
import hostel.util.FactoryConfiguration;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {
    @Override
    public ArrayList<Reservation> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        ArrayList<Reservation> reservations = new ArrayList<>();

        try {
            transaction = session.beginTransaction();
            List<Reservation> reservationList = session.createQuery("FROM Reservation").list();
            reservations.addAll(reservationList);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return reservations;
    }

    @Override
    public boolean save(Reservation entity) throws SQLException, ClassNotFoundException {
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
    public boolean update(Reservation entity) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean b = false;

        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("UPDATE Reservation SET status = :status WHERE res_id = :res_id");
            query.setParameter("status", entity.getStatus());
            query.setParameter("res_id", entity.getRes_id());
            if (query.executeUpdate() > 0) b = true;
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
    public Reservation search(String id) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        Reservation reservation = null;

        try {
            transaction = session.beginTransaction();
            reservation = session.get(Reservation.class, id);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return reservation;
    }

    @Override
    public boolean exist(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean b = false;

        try {
            transaction = session.beginTransaction();
            Reservation reservation = session.load(Reservation.class, id);
            session.delete(reservation);
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
    public String generateNewID() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        String id = "RID-001";

        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("FROM Reservation ORDER BY res_id DESC");
            query.setMaxResults(1);
            Reservation last = (Reservation) query.uniqueResult();
            id = last != null ? last.getRes_id() : id;
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    @Override
    public ArrayList<String> getAvailableRooms(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        ArrayList<String> availableRooms = new ArrayList<>();
        String status = "Available";

        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("SELECT res_id FROM Reservation WHERE room.room_type_id = : room_id AND status = : status");
            query.setParameter("room_id", id);
            query.setParameter("status", status);
            List<String> res_Ids = query.list();
            availableRooms.addAll(res_Ids);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return availableRooms;
    }

    @Override
    public ArrayList<String> getReservedRooms(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        ArrayList<String> availableRooms = new ArrayList<>();

        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("SELECT res_id FROM Reservation WHERE room.room_type_id = : room_id");
            query.setParameter("room_id", id);
            List<String> res_Ids = query.list();
            availableRooms.addAll(res_Ids);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return availableRooms;
    }
}
