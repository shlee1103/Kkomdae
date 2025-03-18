package pizza.kkomdae.repository.rent;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pizza.kkomdae.dto.request.StudentWithRentCond;
import pizza.kkomdae.entity.*;

import java.util.List;

public class CustomRentRepositoryImpl implements CustomRentRepository {

    private final JPAQueryFactory query;

    public CustomRentRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    @Override
    public List<Rent> getRentsByStudentInfo(StudentWithRentCond studentWithRentCond) {
        QRent rent = QRent.rent;
        QStudent student = QStudent.student;
        QDevice device = QDevice.device;
        return query.selectFrom(rent)
                .join(rent.student, student).fetchJoin()
                .join(rent.device, device).fetchJoin()
                .orderBy(student.studentNum.asc())
                .where(isKeyword(studentWithRentCond.getKeyword(), studentWithRentCond.getSearchType()),
                        isRegion(studentWithRentCond.getRegion(), studentWithRentCond.getClassNum())
                )
                .fetch()
                ;
    }

    private Predicate isRegion(String region, Integer classNum) {
        if (region != null && !region.isBlank() && classNum != null) {
            return QStudent.student.region.eq(region).and(QStudent.student.classNum.eq(classNum));
        } else if (region != null && !region.isBlank()) {
            return QStudent.student.region.eq(region);
        }
        return null;
    }

    private Predicate isKeyword(String keyword, String searchType) {
        if (keyword != null && searchType != null && !keyword.isBlank() && !searchType.isBlank()) {
            switch (searchType) {
                case "이름" -> {
                    return QStudent.student.name.like("%" + keyword + "%");
                }
                case "학번" -> {
                    return QStudent.student.studentNum.like("%" + keyword + "%");
                }
            }
        }
        return null;
    }
}
