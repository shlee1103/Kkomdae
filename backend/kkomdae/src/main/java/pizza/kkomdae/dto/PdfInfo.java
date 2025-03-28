package pizza.kkomdae.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PdfInfo {
    private String name;
    private String region;
    private String studentId;
    private String serial;
    private String barcode;
    private String birthday;
    private int laptopCount;
    private int powerCableCount;
    private int adapterCount;
    private int mouseCount;
    private int bagCount;
    private int mousePadCount;
}
