package NetDevops.BuenSabor.service.util;


import NetDevops.BuenSabor.repository.IPedidoDetalleReposiroty;
import NetDevops.BuenSabor.repository.IPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class EstadisticaService {

    @Autowired
    private IPedidoRepository pedidoRepository;
    @Autowired
    private IPedidoDetalleReposiroty pedidoDetalleRepository;

    public Map<LocalDate, Double> getIngresosPorRangoDeDias(LocalDate startDate, LocalDate endDate, Long sucursalId) {
        List<Object[]> results = pedidoRepository.sumTotalesPedidosPorRangoDeDias(startDate, endDate, sucursalId);
        Map<LocalDate, Double> ingresosPorDia = new HashMap<>();
        for (Object[] result : results) {
            LocalDate fecha = (LocalDate) result[0];
            double total = (double) result[1];
            ingresosPorDia.put(fecha, total);
        }
        return ingresosPorDia;
    }

  public Map<YearMonth, Double> getIngresosPorRangoDeMeses(YearMonth startMonth, YearMonth endMonth, Long sucursalId) {
    // Convert YearMonth to LocalDate
    LocalDate startDate = startMonth.atDay(1);
    LocalDate endDate = endMonth.atEndOfMonth();

    List<Object[]> results = pedidoRepository.sumTotalesPedidosPorRangoDeMeses(startDate, endDate, sucursalId);
    Map<YearMonth, Double> ingresosPorMes = new HashMap<>();
    for (Object[] result : results) {
        int month = (int) result[0];
        int year = (int) result[1];
        double total = (double) result[2];
        ingresosPorMes.put(YearMonth.of(year, month), total);
    }
    return ingresosPorMes;
}

}
