package hostel.view.tdm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentTM implements Comparable<StudentTM> {
    private String student_id;
    private String name;
    private String address;
    private String contact_no;
    private LocalDate dob;
    private String gender;

    @Override
    public int compareTo(StudentTM o) {
        return student_id.compareTo(o.getStudent_id());
    }
}
