package NetDevops.BuenSabor.dto.promocion;

import NetDevops.BuenSabor.dto.BaseDto;
import NetDevops.BuenSabor.entities.ImagenArticulo;
import NetDevops.BuenSabor.entities.UnidadMedida;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ArticuloPromocionDto extends BaseDto {
    private String denominacion;
    private String descripcion;
    private Double precioVenta;
    private Integer tiempoEstimadoMinutos;
    private String preparacion;
    protected Set<ImagenArticulo> imagenes = new HashSet<>();
    protected String codigo;
    protected UnidadMedida unidadMedida;

}
