package pizza.kkomdae.repository.student;

import pizza.kkomdae.entity.Student;

import java.util.List;

public interface CustomStudentRepository {
    List<Student> findByKeywordWithStatus(String searchType, String searchKeyword);
}