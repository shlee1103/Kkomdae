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
import pizza.kkomdae.s3.S3Service;
import pizza.kkomdae.security.dto.CustomUserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestResultService {
    private final LapTopTestResultRepository lapTopTestResultRepository;
    private final StudentRepository studentRepository;
    private final DeviceRepository deviceRepository;
    private final PhotoRepository photoRepository;
    private final S3Service s3Service;

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
        // 테스트에 해당하는 laptopTestResult 조회
        LaptopTestResult laptopResult = lapTopTestResultRepository.getReferenceById(testId);
        // 해당하는 테스트에 연관된 Photo 목록 조회
        List<Photo> photos = photoRepository.getPhotosByLaptopTestResult(laptopResult);

        // 각 Photo마다 presigned URL 생성 후 PhotoWithUrl DTO로 변환
        return photos.stream()
                .map(photo -> {
                    String presignedUrl = s3Service.generatePresignedUrl(photo.getName());
                    return new PhotoWithUrl(photo, presignedUrl);
                })
                .collect(Collectors.toList());
    }


    public String getPdfUrl(long testId) {
        LaptopTestResult testResult = lapTopTestResultRepository.findById(testId).orElseThrow();
        return testResult.getPdfUrl();
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
