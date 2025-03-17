package pizza.kkomdae.repository.rent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pizza.kkomdae.entity.Rent;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long>, CustomRentRepository {
}
