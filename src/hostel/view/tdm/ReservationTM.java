package hostel.view.tdm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationTM {
    private String res_id;
    private LocalDate date;
    private String student_id;
    private String name;
    private String room_type_id;
    private String type;
    private String status;
    private String key_money;

    public ReservationTM(String student_id, String name, String room_type_id, String type, String key_money, String status) {
        this.student_id = student_id;
        this.name = name;
        this.room_type_id = room_type_id;
        this.type = type;
        this.key_money = key_money;
        this.status = status;
    }

    public ReservationTM(String res_id, LocalDate date, String student_id, String room_type_id, String status) {
        this.res_id = res_id;
        this.date = date;
        this.student_id = student_id;
        this.room_type_id = room_type_id;
        this.status = status;
    }
}
