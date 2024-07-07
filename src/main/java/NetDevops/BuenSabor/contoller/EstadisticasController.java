package NetDevops.BuenSabor.contoller;

import NetDevops.BuenSabor.service.util.EstadisticaService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasController {

    @Autowired
    private EstadisticaService estadisticaService;

    @GetMapping("/ingresos/dias")
    public Map<LocalDate, Double> getIngresosPorRangoDeDias(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                            @RequestParam Long sucursalId) {
        return estadisticaService.getIngresosPorRangoDeDias(startDate, endDate, sucursalId);
    }

    @GetMapping("/ingresos/meses")
    public Map<YearMonth, Double> getIngresosPorRangoDeMeses(@RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth startMonth,
                                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth endMonth,
                                                             @RequestParam Long sucursalId) {
        return estadisticaService.getIngresosPorRangoDeMeses(startMonth, endMonth, sucursalId);
    }
}