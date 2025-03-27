package pizza.kkomdae.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class LaptopTestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long laptopTestResultId;
    @ManyToOne(fetch = FetchType.LAZY)
    Device device;
    private Boolean release;
    private Boolean keyboardStatus;
    private String failedKeys; // TODO 입력 값에 따라 수정해야함
    private Boolean usbStatus;
    private String failedPorts; // TODO 입력 값에 따라 수정해야함
    private Boolean cameraStatus;
    private Boolean chargerStatus;
    private Boolean batteryReport;
    private String batteryReportUrl;
    private LocalDate date;
    private String pdfUrl;
    private int step = 0;
    @OneToMany(mappedBy = "laptopTestResult")
    private List<Photo> photos;
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;
    // Todo ai 결과를 어떻게 저장할 지 고민

    public LaptopTestResult(Student student) {
        this.student = student;
    }

}
