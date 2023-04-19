package hostel.bo;

import hostel.bo.custom.impl.ReservationBOImpl;
import hostel.bo.custom.impl.RoomBOImpl;
import hostel.bo.custom.impl.StudentBOImpl;
import hostel.bo.custom.impl.UserBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        if (boFactory == null) {
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case STUDENT:
                return new StudentBOImpl();
            case ROOM:
                return new RoomBOImpl();
            case RESERVATION:
                return new ReservationBOImpl();
            case USER:
                return new UserBOImpl();
            default:
                return null;
        }
    }

    public enum BOTypes {
        STUDENT, ROOM, RESERVATION, USER
    }
}
