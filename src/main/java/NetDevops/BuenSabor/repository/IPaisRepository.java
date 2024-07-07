package NetDevops.BuenSabor.repository;

import NetDevops.BuenSabor.entities.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaisRepository  extends JpaRepository<Pais, Long> {
}
