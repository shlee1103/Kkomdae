package pizza.kkomdae.repository;


import pizza.kkomdae.entity.LaptopTestResult;
import pizza.kkomdae.entity.Student;

import java.util.List;

public interface CustomLapTopTestResultRepository {
    List<LaptopTestResult> getByStudent(long studentId);
}
