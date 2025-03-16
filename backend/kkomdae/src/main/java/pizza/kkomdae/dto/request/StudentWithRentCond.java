package pizza.kkomdae.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentWithRentCond {
    private String region;
    private Integer classNum;
    private String searchType; //학번, 이름
    private String keyword;
}
