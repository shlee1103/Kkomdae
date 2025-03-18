package pizza.kkomdae.repository.student;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pizza.kkomdae.entity.QRent;
import pizza.kkomdae.entity.QStudent;
import pizza.kkomdae.entity.Student;

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

    private Predicate isCondition(String searchType, String searchKeyword) {
        if (searchKeyword != null && searchType != null) {
            switch (searchType) {
                case "이름" -> {
                    return QStudent.student.name.like("%" + searchKeyword + "%");
                }
                case "학번" -> {
                    return QStudent.student.studentNum.like("%" + searchKeyword + "%");
                }
                case "지역" -> {
                    return QStudent.student.region.like("%" + searchKeyword + "%");
                }
            }
        }
        return null;
    }
}
