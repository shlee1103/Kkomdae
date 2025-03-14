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
    private int studentNum;
    private String region;
    private int classNum;
    @OneToMany(mappedBy = "student")
    private List<Rent> rent;
}
