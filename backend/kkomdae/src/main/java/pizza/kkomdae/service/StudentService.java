package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pizza.kkomdae.dto.request.StudentWithRentCond;
import pizza.kkomdae.dto.respond.StudentWithRent;
import pizza.kkomdae.dto.respond.UserTestResultRes;
import pizza.kkomdae.entity.Rent;
import pizza.kkomdae.entity.Student;
import pizza.kkomdae.repository.rent.RentRepository;
import pizza.kkomdae.repository.student.StudentRepository;
import pizza.kkomdae.security.etc.JwtProviderForSpringSecurity;
import pizza.kkomdae.ssafyapi.SsafySsoService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final RentRepository rentRepository;
    private final SsafySsoService ssafySsoService;;
    private final JwtProviderForSpringSecurity jwtProvider;

    // 노트북 현황은 반납하지 않은 rent 값이 true인 것이 있으면
    public List<StudentWithRent> findByKeyword(String searchType, String searchKeyword) {
        List<Student> byKeywordWithStatus = studentRepository.findByKeywordWithStatus(searchType, searchKeyword);

        List<StudentWithRent> results = new ArrayList<>();
        for (Student student : byKeywordWithStatus) {
            StudentWithRent tmp = new StudentWithRent(student);
            tmp.setStatus(true);
            for(Rent rent : student.getRent()){
                if(rent.getReleaseDateTime()==null){
                    tmp.setStatus(false);
                    break;
                }
            }
            results.add(tmp);
        }
        return results;
    }

    public List<UserTestResultRes> getUserRentInfo(long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
//        Student student = studentRepository.findByEmail("sskim629@gmail.com"); TODO jwt에서 추출한 이메일로 변경
        StudentWithRentCond cond = new StudentWithRentCond();
        cond.setStudent(student);
        List<Rent> laptopTestResults = rentRepository.getRentsByStudentInfo(cond);
        List<UserTestResultRes> results = new ArrayList<>();
        for (Rent rent : laptopTestResults) {
            results.add(new UserTestResultRes(rent));
        }
        return results;

    }

}
