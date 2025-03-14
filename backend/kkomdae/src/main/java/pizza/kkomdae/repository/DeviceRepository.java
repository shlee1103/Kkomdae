package pizza.kkomdae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pizza.kkomdae.entity.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
}
