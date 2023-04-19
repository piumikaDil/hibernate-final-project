package hostel.view.tdm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomTM implements Comparable<RoomTM> {
    private String room_type_id;
    private String type;
    private String key_money;
    private int qty;

    @Override
    public int compareTo(RoomTM o) {
        return room_type_id.compareTo(o.getRoom_type_id());
    }
}
