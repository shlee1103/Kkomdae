package pizza.kkomdae.repository.laptopresult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pizza.kkomdae.entity.LaptopTestResult;

@Repository
public interface LapTopTestResultRepository extends JpaRepository<LaptopTestResult,Long>, CustomLapTopTestResultRepository {
}
