package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizza.kkomdae.entity.Laptop;
import pizza.kkomdae.repository.laptop.LaptopRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final LaptopRepository laptopRepository;


    public List<Laptop> getLaptops() {
        return laptopRepository.findAll();
    }
}
