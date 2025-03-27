package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pizza.kkomdae.dto.request.SecondStageReq;
import pizza.kkomdae.dto.respond.LaptopTestResultWithStudent;
import pizza.kkomdae.dto.respond.PhotoWithUrl;
import pizza.kkomdae.entity.*;
import pizza.kkomdae.repository.PhotoRepository;
import pizza.kkomdae.repository.device.DeviceRepository;
import pizza.kkomdae.repository.student.StudentRepository;
import pizza.kkomdae.repository.laptopresult.LapTopTestResultRepository;
import pizza.kkomdae.security.dto.CustomUserDetails;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestResultService {
    private final LapTopTestResultRepository lapTopTestResultRepository;
    private final StudentRepository studentRepository;
    private final DeviceRepository deviceRepository;
    private final PhotoRepository photoRepository;

    public List<LaptopTestResultWithStudent> getByStudentOrDevice(Long studentId, Long deviceId, String deviceType) {
        Student referenceStudentById = null;
        if (studentId != null) {
            referenceStudentById = studentRepository.getReferenceById(studentId);
        }
        Device referenceDeviceById = null;
        if (deviceId != null) {
            referenceDeviceById = deviceRepository.getReferenceById(deviceId);
        }
        List<LaptopTestResult> laptopTestResults = lapTopTestResultRepository.findByStudentOrDevice(referenceStudentById, referenceDeviceById);
        List<LaptopTestResultWithStudent> results = new ArrayList<>();
        for (LaptopTestResult laptopTestResult : laptopTestResults) {
            LaptopTestResultWithStudent laptopTestResultWithStudent = new LaptopTestResultWithStudent(laptopTestResult);
            results.add(laptopTestResultWithStudent);
        }
        return results;
    }

    @Transactional
    public long initTest(long userId) {
        Student student = studentRepository.getReferenceById(userId);
        LaptopTestResult laptopTestResult = new LaptopTestResult(student);
        LaptopTestResult testResult = lapTopTestResultRepository.save(laptopTestResult);

        return testResult.getLaptopTestResultId();
    }

    public List<PhotoWithUrl> getPhotos(long testId) {
        LaptopTestResult laptopResult = lapTopTestResultRepository.getReferenceById(testId);
        List<Photo> photos = photoRepository.getPhotosByLaptopTestResult(laptopResult);
        List<PhotoWithUrl> results = new ArrayList<>();
        for (Photo photo : photos) {
            results.add(new PhotoWithUrl(photo));
        }
        return results;
    }


    @Transactional
    public void secondStage(CustomUserDetails userDetails, SecondStageReq secondStageReq) {
        Student student = studentRepository.getReferenceById(userDetails.getUserId());
        LaptopTestResult testResult = lapTopTestResultRepository.findByStudentAndLaptopTestResultId(student, secondStageReq.getTestId());
        if (testResult == null) {
            throw new RuntimeException("저장된 테스트 없음");
        }
        testResult.saveSecondStage(secondStageReq);

    }
}
