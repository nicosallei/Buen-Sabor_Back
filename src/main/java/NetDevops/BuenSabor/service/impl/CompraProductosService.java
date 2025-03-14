package NetDevops.BuenSabor.service.impl;

import NetDevops.BuenSabor.dto.compraProducto.CompraPedidoDto;
import NetDevops.BuenSabor.dto.compraProducto.CompraProductoDto;
import NetDevops.BuenSabor.dto.compraProducto.PedidoDetalleDto;
import NetDevops.BuenSabor.entities.*;
import NetDevops.BuenSabor.enums.Estado;
import NetDevops.BuenSabor.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class CompraProductosService {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private IArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    private IAriticuloInsumoRepository articuloInsumoRepository;
    @Autowired
    private IArticuloRepository articuloRepository;
    @Autowired
    private IPedidoRepository pedidoRepository;
    @Autowired
    private ICategoriaRepository categoriaRepository;
    @Autowired
    private IClienteRepository clienteRepository;

  public List<CompraProductoDto> findArticulosByCategoria(Long categoriaId) {
    // Obtener los artículos directamente asignados a la categoría padre
    List<ArticuloManufacturado> articulosManufacturadosPadre = articuloManufacturadoRepository.findByCategoriaIdAndEliminadoFalse(categoriaId);
    List<ArticuloInsumo> articulosInsumosPadre = articuloInsumoRepository.findByCategoriaIdAndEliminadoFalse(categoriaId);

    List<CompraProductoDto> articulos = new ArrayList<>();

    // Procesar los artículos de la categoría padre
    for (ArticuloManufacturado articulo : articulosManufacturadosPadre) {
        CompraProductoDto dto = convertToDto(articulo);
        articulos.add(dto);
    }
    for (ArticuloInsumo articulo : articulosInsumosPadre) {
        CompraProductoDto dto = convertToDto(articulo);
        articulos.add(dto);
    }

    // Obtener y procesar los artículos de las subcategorías
    Set<Categoria> subcategorias = categoriaRepository.findByCategoriaPadre_IdAndEliminadoFalse(categoriaId);
    for (Categoria subcategoria : subcategorias) {
        List<ArticuloManufacturado> articulosManufacturados = articuloManufacturadoRepository.findByCategoriaIdAndEliminadoFalse(subcategoria.getId());
        List<ArticuloInsumo> articulosInsumos = articuloInsumoRepository.findByCategoriaIdAndEliminadoFalse(subcategoria.getId());



        for (ArticuloInsumo articulo : articulosInsumos) {
            CompraProductoDto dto = convertToDto(articulo);
            articulos.add(dto);
        }
    }

    return articulos;
}

private CompraProductoDto convertToDto(Articulo articulo) {
      ArticuloManufacturado producto = articuloManufacturadoRepository.findById(articulo.getId()).orElse(null);
    CompraProductoDto dto = new CompraProductoDto();
    dto.setId(articulo.getId());
    dto.setDenominacion(articulo.getDenominacion());
    dto.setDescripcion(articulo.getDescripcion());
    dto.setCodigo(articulo.getCodigo());
    dto.setPrecioVenta(articulo.getPrecioVenta());



    dto.setCategoriaId(articulo.getCategoria().getId());
    if (articulo.getSucursal() != null) {
        dto.setSucursalId(articulo.getSucursal().getId());
    }
    return dto;
}

    public CompraProductoDto buscarArticuloPorId(Long id) {
        Articulo articulo = articuloRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Articulo no encontrado con id: " + id));

        CompraProductoDto dto = new CompraProductoDto();
        dto.setId(articulo.getId());
        dto.setDenominacion(articulo.getDenominacion());
        dto.setDescripcion(articulo.getDescripcion());
        dto.setCodigo(articulo.getCodigo());
        dto.setPrecioVenta(articulo.getPrecioVenta());
        dto.setImagenes(new ArrayList<>(articulo.getImagenes()));
        dto.setCategoriaId(articulo.getCategoria().getId());
        if (articulo.getSucursal() != null) {
            dto.setSucursalId(articulo.getSucursal().getId());
        }
        return dto;

    }




    public CompraPedidoDto crearPedido(CompraPedidoDto compraPedidoDto) throws Exception {
    try {
        Pedido pedido = new Pedido();
        // Set other properties...
        pedido.setFechaPedido(LocalDate.now());
        pedido.setHora(LocalTime.now());
        pedido.setCliente(clienteRepository.findById(compraPedidoDto.getCliente().getId())
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado con id: " + compraPedidoDto.getCliente().getId())));

        List<PedidoDetalle> pedidoDetalles = new ArrayList<>();
        for (PedidoDetalleDto detalleDto : compraPedidoDto.getPedidoDetalle()) {
            Articulo articulo = articuloRepository.findById(detalleDto.getProducto().getId())
                    .orElseThrow(() -> new NoSuchElementException("Articulo no encontrado con id: " + detalleDto.getProducto().getId()));


            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setArticulo(articulo);
            detalle.setCantidad(detalleDto.getCantidad());
            detalle.setPedido(pedido);
            pedidoDetalles.add(detalle);
        }
        pedido.setPedidoDetalle(pedidoDetalles);
        pedido.setSucursal(pedidoDetalles.get(0).getArticulo().getSucursal());
        pedido.setEstado(Estado.PENDIENTE);
        pedidoRepository.save(pedido);
        CompraPedidoDto pedidoDto = modelMapper.map(pedido, CompraPedidoDto.class);

        return pedidoDto;
    } catch (Exception e) {
        throw new Exception(e.getMessage());
    }
}


}
