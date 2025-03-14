package pizza.kkomdae.entity;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Laptop extends Device{
    String serialNum;
    String barcodeNum;
    String modelCode;
}
