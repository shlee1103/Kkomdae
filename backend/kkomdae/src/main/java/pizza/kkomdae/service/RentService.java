package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pizza.kkomdae.dto.request.StudentWithRentCond;
import pizza.kkomdae.dto.respond.StudentWithRent;
import pizza.kkomdae.entity.*;
import pizza.kkomdae.repository.rent.RentRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentService {
    private final RentRepository rentRepository;


    public List<StudentWithRent> getRentByStudent(StudentWithRentCond studentWithRentCond) {
        List<Rent> rents = rentRepository.getRentByStudentInfo(studentWithRentCond);

        List<StudentWithRent> results = new ArrayList<>();
        Student student = null;
        StudentWithRent studentWithRent = null;
        for (Rent rent : rents) {
            if (student == null) {
                student = rent.getStudent();
                studentWithRent = new StudentWithRent(student);
            } else if (!student.equals(rent.getStudent())) {
                results.add(studentWithRent);
                student = rent.getStudent();
                studentWithRent = new StudentWithRent(student);
            }
            StudentWithRent.DeviceRentHistory deviceRentHistory = new StudentWithRent.DeviceRentHistory(rent.getDevice());
            if(!rent.isRelease())studentWithRent.setStatus(false);
            studentWithRent.getDeviceRentHistory().add(deviceRentHistory);
            log.info("{} {} {}",rent.getDevice().isRelease(),deviceRentHistory.getModelCode(), rent.getDevice().getDeviceType());

        }
        if(studentWithRent!=null)results.add(studentWithRent);
        return results;
    }
}
