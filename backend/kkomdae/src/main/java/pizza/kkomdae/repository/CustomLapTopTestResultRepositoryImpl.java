package pizza.kkomdae.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pizza.kkomdae.entity.*;

import java.util.List;

public class CustomLapTopTestResultRepositoryImpl implements CustomLapTopTestResultRepository{

    private final JPAQueryFactory query;

    public CustomLapTopTestResultRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<LaptopTestResult> getByStudent(Student student) {
        QLaptopTestResult laptopTestResult = QLaptopTestResult.laptopTestResult;
        QDevice device = QDevice.device;
        QRent rent = QRent.rent;
        QStudent qStudent = QStudent.student;
        return query.select(laptopTestResult)
                .from(laptopTestResult)
                .leftJoin(device).on(laptopTestResult.device.eq(device))
                .leftJoin(rent).on(device.eq(rent.device))
//                .leftJoin(qStudent).on(rent.student.eq(qStudent))
                .where(rent.student.eq(student))
                .fetch();
    }
}
