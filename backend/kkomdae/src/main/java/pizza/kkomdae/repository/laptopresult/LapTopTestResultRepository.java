package pizza.kkomdae.repository.laptopresult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pizza.kkomdae.entity.Device;
import pizza.kkomdae.entity.LaptopTestResult;
import pizza.kkomdae.entity.Student;

import java.util.List;

@Repository
public interface LapTopTestResultRepository extends JpaRepository<LaptopTestResult,Long>, CustomLapTopTestResultRepository {

    LaptopTestResult findByStudentAndStepIsLessThan(Student student, int stepIsLessThan);

    LaptopTestResult findByStudentAndLaptopTestResultId(Student student, long laptopTestResultId);

    LaptopTestResult findByStudentAndStepIsLessThanAndDeviceIsNull(Student student, int stepIsLessThan);
}
