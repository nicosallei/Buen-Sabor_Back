package NetDevops.BuenSabor.dto.pedido;

import NetDevops.BuenSabor.dto.BaseDto;

import lombok.Data;

@Data
public class PedidoDetalleDto extends BaseDto {
    private ArticuloManufacturadoDto articulo;
    private Integer cantidad;

}
