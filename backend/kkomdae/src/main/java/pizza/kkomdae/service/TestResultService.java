package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizza.kkomdae.entity.LaptopTestResult;
import pizza.kkomdae.entity.Student;
import pizza.kkomdae.repository.LapTopTestResultRepository;
import pizza.kkomdae.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestResultService {
    private final LapTopTestResultRepository lapTopTestResultRepository;
    private final StudentRepository studentRepository;
    public List<LaptopTestResult> getByStudent(long studentId) {
        Student student = studentRepository.getReferenceById(studentId);
        return lapTopTestResultRepository.getByStudent(student);
    }
}
