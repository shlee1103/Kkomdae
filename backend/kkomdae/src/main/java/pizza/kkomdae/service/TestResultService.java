package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizza.kkomdae.entity.Device;
import pizza.kkomdae.entity.LaptopTestResult;
import pizza.kkomdae.entity.Student;
import pizza.kkomdae.repository.device.DeviceRepository;
import pizza.kkomdae.repository.StudentRepository;
import pizza.kkomdae.repository.laptopresult.LapTopTestResultRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestResultService {
    private final LapTopTestResultRepository lapTopTestResultRepository;
    private final StudentRepository studentRepository;
    private final DeviceRepository deviceRepository;

    public List<LaptopTestResult> getByStudentOrDevice(Long studentId, Long deviceId, String deviceType) {
        Student referenceStudentById = null;
        if (studentId != null) {
            referenceStudentById = studentRepository.getReferenceById(studentId);
        }
        Device referenceDeviceById = null;
        if (deviceId != null) {
            referenceDeviceById = deviceRepository.getReferenceById(deviceId);
        }
        return lapTopTestResultRepository.findByStudentOrDevice(referenceStudentById, referenceDeviceById);
    }


}
