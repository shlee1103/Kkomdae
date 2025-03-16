package pizza.kkomdae.repository.laptop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pizza.kkomdae.entity.Laptop;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop,Long> {
}
