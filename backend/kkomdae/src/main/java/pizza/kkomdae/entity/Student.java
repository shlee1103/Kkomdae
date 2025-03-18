package pizza.kkomdae.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long studentId;
    private String name;
    private String studentNum;
    private String region;
    private int classNum;
    @OneToMany(mappedBy = "student")
    private List<Rent> rent;
    @OneToMany(mappedBy = "student")
    private List<LaptopTestResult> laptopTestResults;
}
