package NetDevops.BuenSabor.repository;

import NetDevops.BuenSabor.entities.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IPedidoDetalleReposiroty extends JpaRepository<PedidoDetalle, Long> {

}
