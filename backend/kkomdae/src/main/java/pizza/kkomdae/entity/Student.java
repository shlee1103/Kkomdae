package pizza.kkomdae.entity;

import jakarta.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long studentId;
    private String name;
    private int studentNum;
    private String region;
    private int classNum;
    @OneToOne
    @JoinColumn(name = "rent_id")
    private Rent rent;
}
