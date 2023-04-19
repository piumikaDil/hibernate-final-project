package hostel.dao.custom.impl;

import hostel.dao.custom.UserDAO;
import hostel.entity.User;
import hostel.util.FactoryConfiguration;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    @Override
    public ArrayList<User> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        ArrayList<User> users = new ArrayList<>();

        try {
            transaction = session.beginTransaction();
            List<User> userList = session.createQuery("FROM User").list();
            users.addAll(userList);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return users;
    }

    @Override
    public boolean save(User entity) throws SQLException, ClassNotFoundException {
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
    public boolean update(User entity) throws SQLException, ClassNotFoundException {
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
    public User search(String id) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        User user = null;

        try {
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean b = false;

        try {
            transaction = session.beginTransaction();
            if (session.get(User.class, id) != null) b = true;
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
            User user = session.load(User.class, id);
            session.delete(user);
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
        String id = null;

        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("FROM User ORDER BY user_id DESC");
            query.setMaxResults(1);
            User last = (User) query.uniqueResult();
            id = last != null ? last.getUser_id() : id;
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
    public User verifyUser(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        User user = null;

        try {
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }
}
