package pizza.kkomdae.entity;

import jakarta.persistence.*;

@Entity
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rentId;
    private boolean release;
    @OneToOne(mappedBy = "rent")
    private Student student;
}
