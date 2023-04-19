package hostel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationDTO implements Serializable {
    List<ReservationDTO> reservationDTOS;
    private String res_id;
    private LocalDate date;
    private String student_id;
    private String room_type_id;
    private String status;

    public ReservationDTO(String res_id, LocalDate date, String student_id, String room_type_id, String status) {
        this.res_id = res_id;
        this.date = date;
        this.student_id = student_id;
        this.room_type_id = room_type_id;
        this.status = status;
    }

    public ReservationDTO(List<ReservationDTO> reservationDTOS) {
        this.reservationDTOS = reservationDTOS;
    }
}
