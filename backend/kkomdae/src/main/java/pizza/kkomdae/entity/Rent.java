package pizza.kkomdae.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rentId;
    private boolean release;
    @ManyToOne
    private Student student;
    @ManyToOne
    private Device device;

}
