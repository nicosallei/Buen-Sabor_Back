package NetDevops.BuenSabor.dto.articuloManufacturado;

import NetDevops.BuenSabor.dto.BaseDto;


import NetDevops.BuenSabor.dto.articuloInsumo.ArticuloInsumoDto;
import lombok.Data;

@Data
public class ArticuloManufacturadoDetalleDto extends BaseDto {

    private ArticuloInsumoDto articuloInsumoDto;
    private Integer cantidad;
    private ArticuloManufacturadoDto articuloManufacturadoDto;
}
