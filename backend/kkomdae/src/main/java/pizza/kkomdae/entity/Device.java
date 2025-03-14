package pizza.kkomdae.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pizza.kkomdae.enums.DeviceType;

import java.util.List;

@Entity
@Getter
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
    @OneToMany(mappedBy = "device")
    private List<Rent> rent;
}
