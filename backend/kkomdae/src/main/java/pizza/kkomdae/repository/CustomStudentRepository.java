package pizza.kkomdae.repository;

import pizza.kkomdae.entity.Student;

import java.util.List;

public interface CustomStudentRepository {
    List<Student> findByKeyword(String searchType, String searchKeyword);
}
