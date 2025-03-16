package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizza.kkomdae.dto.request.DeviceCond;
import pizza.kkomdae.dto.respond.DeviceWithStatus;
import pizza.kkomdae.entity.Device;
import pizza.kkomdae.entity.Laptop;
import pizza.kkomdae.repository.device.DeviceRepository;
import pizza.kkomdae.repository.laptop.LaptopRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final LaptopRepository laptopRepository;
    private final DeviceRepository deviceRepository;

    public List<Laptop> getLaptops() {
        return laptopRepository.findAll();
    }

    public List<DeviceWithStatus> getDevicesWithCond(DeviceCond deviceCond) {
        List<Device> deviceWithStatusByCond = deviceRepository.getDeviceWithStatusByCond(deviceCond);
        List<DeviceWithStatus> results = new ArrayList<>();
        for (Device device : deviceWithStatusByCond) {
            results.add(new DeviceWithStatus(device));
        }
        return results;
    }
}
