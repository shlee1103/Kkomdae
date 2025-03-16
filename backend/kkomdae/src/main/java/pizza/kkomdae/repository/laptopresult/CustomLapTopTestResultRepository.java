package pizza.kkomdae.repository.laptopresult;


import pizza.kkomdae.entity.LaptopTestResult;

import java.util.List;

public interface CustomLapTopTestResultRepository {
    List<LaptopTestResult> getByStudent(long studentId);
}
