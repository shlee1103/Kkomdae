package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pizza.kkomdae.dto.request.LoginInfo;
import pizza.kkomdae.dto.request.StudentWithRentCond;
import pizza.kkomdae.dto.respond.LoginRes;
import pizza.kkomdae.dto.respond.StudentWithRent;
import pizza.kkomdae.dto.respond.UserTestResultRes;
import pizza.kkomdae.entity.Rent;
import pizza.kkomdae.entity.Student;
import pizza.kkomdae.repository.laptopresult.LapTopTestResultRepository;
import pizza.kkomdae.repository.rent.RentRepository;
import pizza.kkomdae.repository.student.StudentRepository;
import pizza.kkomdae.ssafyapi.SsafySsoService;
import pizza.kkomdae.ssafyapi.UserInfo;
import pizza.kkomdae.ssafyapi.UserRequestForSso;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final RentRepository rentRepository;
    private final LapTopTestResultRepository lapTopTestResultRepository;
    private final SsafySsoService ssafySsoService;

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

    public LoginRes login(LoginInfo loginInfo) {
        return null;
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

    public long checkStudentExist(UserRequestForSso loginUserInfo) {
        Student student = studentRepository.findByEmail(loginUserInfo.getLoginId());
        if (student == null) {
            UserInfo userInfo = ssafySsoService.getUserInfo(loginUserInfo.getUserId());
            log.info("{} {} {} {}", userInfo.getName(),userInfo.getEmail(),userInfo.getEntRegn(),userInfo.getClss());
            student = new Student();
            student.setName(loginUserInfo.getName());
            student.setEmail(userInfo.getEmail());
            student.setEdu(userInfo.getEdu());
            student.setRegion(userInfo.getEntRegn());
            student.setClassNum(userInfo.getClss());
            student.setRetireYn(userInfo.getRetireYn());
            studentRepository.save(student);
        }
        return student.getStudentId();
    }
}
