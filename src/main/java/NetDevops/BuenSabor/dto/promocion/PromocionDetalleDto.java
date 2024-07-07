package NetDevops.BuenSabor.dto.promocion;

import NetDevops.BuenSabor.dto.BaseDto;
import NetDevops.BuenSabor.entities.ImagenPromocion;
import lombok.Data;

@Data
public class PromocionDetalleDto extends BaseDto{

    private int cantidad;
    private ArticuloPromocionDto articuloManufacturadoDto;
    private ImagenPromocion imagenPromocion;

}
