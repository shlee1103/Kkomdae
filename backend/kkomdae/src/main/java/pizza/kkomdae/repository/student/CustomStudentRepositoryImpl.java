package pizza.kkomdae.repository.student;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pizza.kkomdae.dto.request.StudentWithRentCond;
import pizza.kkomdae.entity.*;

import java.util.List;

public class CustomStudentRepositoryImpl implements CustomStudentRepository {

    private final JPAQueryFactory query;

    public CustomStudentRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Student> findByKeywordWithStatus(String searchType, String searchKeyword) {
        return query.select(QStudent.student)
                .from(QStudent.student)
                .leftJoin(QStudent.student.rent, QRent.rent).fetchJoin()
                .where(isCondition(searchType, searchKeyword))
                .orderBy(QStudent.student.studentNum.asc())
                .fetch();
    }

    @Override
    public List<Student> getStudentsByStudentInfo(StudentWithRentCond studentWithRentCond) {
        QRent rent = QRent.rent;
        QStudent student = QStudent.student;
        QDevice device = QDevice.device;
        return query.selectFrom(student)
                .distinct()
                .leftJoin(student.rent, rent).fetchJoin()
                .leftJoin(rent.device, device).fetchJoin()
//                .join(rent.device.laptopTestResults, QLaptopTestResult.laptopTestResult).fetchJoin()
//                .join(student.laptopTestResults, QLaptopTestResult.laptopTestResult).fetchJoin()
                .where(isKeyword(studentWithRentCond.getKeyword(), studentWithRentCond.getSearchType()),
                        isRegion(studentWithRentCond.getRegion(), studentWithRentCond.getClassName())
                        , isStudent(studentWithRentCond.getStudent()),
                        isClass(studentWithRentCond.getClassName(), studentWithRentCond.getRegion())
                )
                .orderBy(student.studentNum.asc())
                .fetch()
                ;
    }

    private Predicate isClass(String className, String region) {
        if (className != null && region != null && !className.isBlank() && !region.isBlank()) {
            return QStudent.student.classNum.like("%" + region + className + "%");
        }
        return null;
    }

    private Predicate isStudent(Student student) {
        if (student != null) {
            return QStudent.student.eq(student);
        }
        return null;
    }

    private Predicate isRegion(String region, String classNum) {
        if (region != null && !region.isBlank() && classNum != null) {
            return QStudent.student.region.eq(region).and(QStudent.student.classNum.like("%" + region + classNum + "%"));
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
                case "학생고유번호" -> {
                    return QStudent.student.studentId.eq(Long.valueOf(keyword));
                }
            }
        }
        return null;
    }

    private Predicate isCondition(String searchType, String searchKeyword) {
        if (searchKeyword != null && searchType != null) {
            switch (searchType) {
                case "이름" -> {
                    return QStudent.student.name.like("%" + searchKeyword + "%");
                }
                case "학생고유번호" -> {
                    return QStudent.student.studentId.eq(Long.valueOf(searchKeyword));
                }
                case "지역" -> {
                    return QStudent.student.region.like("%" + searchKeyword + "%");
                }
            }
        }
        return null;
    }
}
