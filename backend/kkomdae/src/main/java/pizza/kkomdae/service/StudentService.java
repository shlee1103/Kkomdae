package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizza.kkomdae.entity.Student;
import pizza.kkomdae.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> findByKeyword(String searchType, String searchKeyword) {
        return studentRepository.findByKeyword(searchType,searchKeyword);
    }
}
