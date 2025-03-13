package pizza.kkomdae.entity;

import jakarta.persistence.*;
import pizza.kkomdae.enums.DeviceType;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "device_type")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long deviceId;
    private boolean release;
    @OneToMany(mappedBy = "device")
    private List<PhoneTestResult> phoneTestResults;
    @OneToMany(mappedBy = "device")
    private List<LaptopTestResult> laptopTestResults;
}
