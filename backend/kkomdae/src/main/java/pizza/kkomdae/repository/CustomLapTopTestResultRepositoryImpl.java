package pizza.kkomdae.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.expression.spel.ast.Projection;
import pizza.kkomdae.entity.*;

import java.util.List;

public class CustomLapTopTestResultRepositoryImpl implements CustomLapTopTestResultRepository {

    private final JPAQueryFactory query;

    public CustomLapTopTestResultRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<LaptopTestResult> getByStudent(long studentId) {
        QLaptopTestResult laptopTestResult = QLaptopTestResult.laptopTestResult;
        QDevice device = QDevice.device;
        QRent rent = QRent.rent;
        return query
                .selectFrom(laptopTestResult)
                .join(laptopTestResult.device, device).fetchJoin()
                .join(device.rent, rent)
                .where(rent.student.studentId.eq(studentId))
                .fetch();
    }
}
