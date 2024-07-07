package NetDevops.BuenSabor.dto.articuloManufacturado;

import NetDevops.BuenSabor.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ArticuloManufacturadoTablaDto extends BaseDto {
    private String codigo;
    private String denominacion;
    private String imagen;
    private Double precioVenta;
    private String descripcion;
    private Integer tiempoEstimadoCocina;
}
