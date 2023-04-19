package hostel.dao.custom.impl;

import hostel.dao.custom.QueryDAO;
import hostel.util.FactoryConfiguration;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public ArrayList<Object[]> search(String search) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        ArrayList<Object[]> arrayList = new ArrayList<>();

        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("SELECT res.res_id, res.date, st.student_id, room.room_type_id, res.status FROM Reservation res INNER JOIN Room room ON res.room.room_type_id = room.room_type_id INNER JOIN Student st ON res.student.student_id = st.student_id WHERE res.res_id = : res_id OR st.student_id = : st_id OR room.room_type_id = : room_id OR res.status = : status");
            query.setParameter("res_id", search);
            query.setParameter("st_id", search);
            query.setParameter("room_id", search);
            query.setParameter("status", search);
            List<Object[]> list = query.list();
            arrayList.addAll(list);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return arrayList;
    }
}
