package pizza.kkomdae.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rentId;
    private LocalDateTime releaseDateTime;
    private LocalDateTime rentDateTime;
    @ManyToOne
    private Student student;
    @ManyToOne
    private Device device;

}
