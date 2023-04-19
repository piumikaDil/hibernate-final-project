package hostel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomDTO implements Serializable {
    private String room_type_id;
    private String type;
    private String key_money;
    private int qty;
}
