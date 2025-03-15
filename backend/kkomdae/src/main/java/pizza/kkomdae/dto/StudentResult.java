package pizza.kkomdae.dto;


import lombok.Getter;
import lombok.Setter;
import pizza.kkomdae.entity.Student;

import java.util.List;

@Getter
@Setter
public class StudentResult {
    private long studentId;
    private int studentNum;
    private String name;
    private String region;
    private boolean status;
    private List<DeviceRentHistory> deviceRentHistory;

    public StudentResult(Student student) {
        this.studentId = student.getStudentId();
        this.studentNum = student.getStudentNum();
        this.name = student.getName();
        this.region = student.getRegion();

    }

    @Getter
    public static class DeviceRentHistory{
        private String type;
        private String modelCode;
        private String serialNum;
        private boolean status;
    }
}
