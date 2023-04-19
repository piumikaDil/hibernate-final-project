package hostel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomEntity {
    private String student_id;
    private String name;
    private String address;
    private String contact_no;
    private LocalDate dob;
    private String gender;

    private String room_id;
    private String type;
    private Double key_money;
    private int qty;

    private String res_id;
    private LocalDate date;
    private String status;
}

