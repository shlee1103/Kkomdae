package pizza.kkomdae.repository.laptopresult;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pizza.kkomdae.entity.*;

import java.util.List;

public class CustomLapTopTestResultRepositoryImpl implements CustomLapTopTestResultRepository {

    private final JPAQueryFactory query;

    public CustomLapTopTestResultRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<LaptopTestResult> findByStudentOrDevice(Student student, Device device) {
        return query.selectFrom(QLaptopTestResult.laptopTestResult)
                .join(QLaptopTestResult.laptopTestResult.device, QDevice.device)
                .join(QDevice.device.rent, QRent.rent)
                .join(QRent.rent.student, QStudent.student)
                .where(isCond(student,device))
                .fetch()
                ;
    }

    private Predicate isCond(Student student, Device device) {
        if(student!=null && device!=null){
            return QStudent.student.eq(student).and(QDevice.device.eq(device));
        } else if (student!=null) {
            return QStudent.student.eq(student);
        } else if (device!=null) {
            return QDevice.device.eq(device);
        }
        return null;
    }
}
