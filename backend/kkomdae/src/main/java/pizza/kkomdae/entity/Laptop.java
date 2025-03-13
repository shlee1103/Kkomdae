package pizza.kkomdae.entity;

import jakarta.persistence.Entity;

@Entity
public class Laptop extends Device{
    String serialNum;
    String barcodeNum;
    String modelCode;
}
