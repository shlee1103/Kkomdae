package pizza.kkomdae.repository.rent;


import pizza.kkomdae.dto.RentResults;
import pizza.kkomdae.dto.request.StudentWithRentCond;
import pizza.kkomdae.entity.Rent;

import java.util.List;

public interface CustomRentRepository {
    List<Rent> getRentByStudentInfo(StudentWithRentCond studentWithRentCond);
}
