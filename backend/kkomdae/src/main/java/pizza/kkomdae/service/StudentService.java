package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizza.kkomdae.dto.StudentResult;
import pizza.kkomdae.entity.Rent;
import pizza.kkomdae.entity.Student;
import pizza.kkomdae.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    // 노트북 현황은 반납하지 않은 rent 값이 true인 것이 있으면
    public List<StudentResult> findByKeyword(String searchType, String searchKeyword) {
        List<Student> byKeywordWithStatus = studentRepository.findByKeywordWithStatus(searchType, searchKeyword);

        List<StudentResult> results = new ArrayList<>();
        for (Student student : byKeywordWithStatus) {
            StudentResult tmp = new StudentResult(student);
            tmp.setStatus(true);
            for(Rent rent : student.getRent()){
                if(!rent.isRelease()){
                    tmp.setStatus(false);
                    break;
                }
            }
            results.add(tmp);
        }
        return results;
    }
}
