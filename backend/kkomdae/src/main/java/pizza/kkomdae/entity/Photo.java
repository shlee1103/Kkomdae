package pizza.kkomdae.entity;

import jakarta.persistence.*;
import pizza.kkomdae.enums.PhotoType;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long photoId;
    private String name;
    @Enumerated(EnumType.STRING)
    private PhotoType type;
    private String url;
    @ManyToOne
    private LaptopTestResult laptopTestResult;
    @ManyToOne
    private PhoneTestResult phoneTestResult;
}

