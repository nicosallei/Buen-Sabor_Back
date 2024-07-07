package NetDevops.BuenSabor.service.impl;

import NetDevops.BuenSabor.dto.categoria.*;
import NetDevops.BuenSabor.dto.sucursal.SucursalSimpleDto;
import NetDevops.BuenSabor.entities.Articulo;
import NetDevops.BuenSabor.entities.Categoria;
import NetDevops.BuenSabor.entities.Empresa;
import NetDevops.BuenSabor.entities.Sucursal;
import NetDevops.BuenSabor.repository.IArticuloRepository;
import NetDevops.BuenSabor.repository.ICategoriaRepository;
import NetDevops.BuenSabor.repository.IEmpresaRepository;
import NetDevops.BuenSabor.repository.ISucursalRepository;
import NetDevops.BuenSabor.service.ICategoriaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoriaService implements ICategoriaService {
    @Autowired
    private ICategoriaRepository categoriaRepository;
    @Autowired
    private IArticuloRepository articuloRepository;
    @Autowired
    private ISucursalRepository sucursalRepository;

@Override
public Categoria cargar(Categoria categoria) throws Exception {
    if (categoriaRepository.existsByDenominacionAndEliminadoFalse(categoria.getDenominacion())) {
        throw new Exception("Ya existe una categoria con esa denominacion");
    }
    List<Sucursal> sucursales = new ArrayList<>();
    for (Sucursal sucursal: categoria.getSucursales()) {
        sucursales.add(sucursalRepository.findById(sucursal.getId())
                .orElseThrow(() -> new Exception("No existe una sucursal con el id " + sucursal.getId())));
        System.out.println(sucursal.getId());
    }
    categoria.setSucursales(sucursales);
    return categoriaRepository.save(categoria);
}

@Override
public Categoria actualizarCategoriaPadre(Long id, Categoria nuevaCategoria) throws Exception {
    try {
        if (!categoriaRepository.existsByIdAndEliminadoFalse(id)) {
            throw new Exception("No existe una categoria con ese id");
        }
        if (categoriaRepository.existsByDenominacionAndEliminadoFalse(nuevaCategoria.getDenominacion())) {
            throw new Exception("Ya existe una categoria con esa denominacion");
        }
        Categoria categoria = categoriaRepository.findByIdAndEliminadoFalse(id);

        // Actualizar la denominación
        categoria.setDenominacion(nuevaCategoria.getDenominacion());

        // Manejar las subcategorías
        for (Categoria subCategoria : new HashSet<>(categoria.getSubCategorias())) {
            if (!nuevaCategoria.getSubCategorias().contains(subCategoria)) {
                if (!subCategoria.getArticulos().isEmpty()) {
                    throw new Exception("Hay subcategorias que tienen articulos");
                }
                categoria.getSubCategorias().remove(subCategoria);
            }
        }
        for (Categoria subCategoria : nuevaCategoria.getSubCategorias()) {
            if (categoriaRepository.existsByDenominacionAndEliminadoFalse(subCategoria.getDenominacion())) {
                throw new Exception("Ya existe una subcategoria con esa denominacion");
            }
            if (!categoria.getSubCategorias().contains(subCategoria)) {
                categoria.getSubCategorias().add(subCategoria);
            }
        }

        return categoriaRepository.save(categoria);
    } catch (Exception e) {
        throw new Exception(e.getMessage());
    }
}

    @Override
    public Set<Categoria> lista() throws Exception {
        try {
            if (categoriaRepository.findAll().isEmpty()) {
                throw new Exception("No hay categorias cargadas");
            }
            return categoriaRepository.findAllByEliminadoFalse();

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Categoria buscar(Long id) throws Exception {
        try {
            if (!categoriaRepository.existsByIdAndEliminadoFalse(id)) {
                throw new Exception("No existe una categoria con ese id");
            }
            return categoriaRepository.findByIdAndEliminadoFalse(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean eliminar(Long id) throws Exception {
        try {
            if (!categoriaRepository.existsByIdAndEliminadoFalse(id)) {
                throw new Exception("No existe una categoria con ese id");
            }
            Categoria categoria = categoriaRepository.findByIdAndEliminadoFalse(id);
            categoria.setEliminado(true);
            categoriaRepository.save(categoria);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean eliminarSubCategoria(Long idCategoria, Long idSubCategoria) throws Exception {
        try {
            if (!categoriaRepository.existsByIdAndEliminadoFalse(idCategoria)) {
                throw new Exception("No existe una categoria con ese id");
            }
            Categoria categoria = categoriaRepository.findByIdAndEliminadoFalse(idCategoria);
            Categoria subCategoria = null;
            for (Categoria subCat : categoria.getSubCategorias()) {
                if (subCat.getId().equals(idSubCategoria)) {
                    subCategoria = subCat;
                    break;
                }
            }
            if (subCategoria == null) {
                throw new Exception("No existe una subcategoria con ese id en la categoria especificada");
            }
            categoria.getSubCategorias().remove(subCategoria);
            categoriaRepository.save(categoria);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

//    public Categoria agregarArticulo(Long id, Articulo articulo) throws Exception {
//        try {
//            if (!categoriaRepository.existsByIdAndEliminadoFalse(id)) {
//                throw new Exception("No existe una categoria con ese id");
//            }
//            Categoria categoria = categoriaRepository.findByIdAndEliminadoFalse(id);
//
//            categoria.getArticulos().add(articulo);
//            return categoriaRepository.save(categoria);
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }

    public Categoria agregarArticulo(Long idCategoria, Long idArticulo) throws Exception {
        try {
            if (!categoriaRepository.existsByIdAndEliminadoFalse(idCategoria)) {
                throw new Exception("No existe una categoria con ese id");
            }
            if (!articuloRepository.existsById(idArticulo)) {
                throw new Exception("No existe un articulo con ese id");
            }
            Categoria categoria = categoriaRepository.findByIdAndEliminadoFalse(idCategoria);
            Articulo articulo = articuloRepository.findById(idArticulo).orElse(null);

            categoria.getArticulos().add(articulo);
            return categoriaRepository.save(categoria);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Categoria eliminarArticuloDeSubCategoria(Long idSubCategoria, Long idArticulo) throws Exception {
        try {
            if (!categoriaRepository.existsByIdAndEliminadoFalse(idSubCategoria)) {
                throw new Exception("No existe una subcategoria con ese id");
            }
            if (!articuloRepository.existsById(idArticulo)) {
                throw new Exception("No existe un articulo con ese id");
            }
            Categoria subCategoria = categoriaRepository.findByIdAndEliminadoFalse(idSubCategoria);
            Articulo articulo = articuloRepository.findById(idArticulo).orElse(null);

            subCategoria.getArticulos().remove(articulo);
            return categoriaRepository.save(subCategoria);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Categoria actualizarSubCategoria(Long idSubCategoria, Categoria nuevaSubCategoria) throws Exception {
        try {
            if (!categoriaRepository.existsByIdAndEliminadoFalse(idSubCategoria)) {
                throw new Exception("No existe una subcategoria con ese id");
            }
            Categoria subCategoria = categoriaRepository.findByIdAndEliminadoFalse(idSubCategoria);

            // Actualizar la denominación
            subCategoria.setDenominacion(nuevaSubCategoria.getDenominacion());

            // Manejar los artículos
            for (Articulo articulo : new HashSet<>(subCategoria.getArticulos())) {
                if (!nuevaSubCategoria.getArticulos().contains(articulo)) {
                    subCategoria.getArticulos().remove(articulo);
                }
            }
            for (Articulo articulo : nuevaSubCategoria.getArticulos()) {
                if (!subCategoria.getArticulos().contains(articulo)) {
                    subCategoria.getArticulos().add(articulo);
                }
            }

            return categoriaRepository.save(subCategoria);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



    public Categoria agregarSubCategoria(Long id, Categoria subCategoria) throws Exception {
        try {
            if (!categoriaRepository.existsByIdAndEliminadoFalse(id)) {
                throw new Exception("No existe una categoria con ese id");
            }
            Categoria categoria = categoriaRepository.findByIdAndEliminadoFalse(id);
            subCategoria.setCategoriaPadre(categoria);

            return categoriaRepository.save(subCategoria);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Obtengo las categorias con subcategorias
    public Set<Categoria> obtenerCategoriasConSubCategorias() throws Exception {
        try {
            if (categoriaRepository.findAllByEliminadoFalse().isEmpty()) {
                throw new Exception("No hay categorias no eliminadas");
            }
            return categoriaRepository.findAllByEliminadoFalse();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean reactivate(Long id) throws Exception {
        try {
            if (categoriaRepository.existsById(id)) {
                Categoria categoria = categoriaRepository.findById(id).get();
                if (categoria.isEliminado()) {
                    categoria.setEliminado(false);

                    // Reactivar los Articulos asociados
                    for (Articulo articulo : categoria.getArticulos()) {
                        if (articulo.isEliminado()) {
                            articulo.setEliminado(false);
                        }
                    }

                    // Reactivar las SubCategorias asociadas
                    for (Categoria subCategoria : categoria.getSubCategorias()) {
                        if (subCategoria.isEliminado()) {
                            subCategoria.setEliminado(false);
                        }
                    }

                    categoriaRepository.save(categoria);
                    return true;
                } else {
                    throw new Exception("La categoria con el id proporcionado no está eliminada");
                }
            } else {
                throw new Exception("No existe la categoria con el id proporcionado");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
@Override
public Set<CategoriaDto> traerTodo() throws Exception {
    try {
        Set<Categoria> listaCategoriaOriginal = categoriaRepository.ListaCategorias();
        Set<CategoriaDto> listaDto = new HashSet<>();
        for (Categoria lista: listaCategoriaOriginal){
            CategoriaDto categoriadto = new CategoriaDto();
            categoriadto.setDenominacion(lista.getDenominacion());
            categoriadto.setUrlIcono(lista.getUrlIcono());
            categoriadto.setId(lista.getId());
            categoriadto.setEliminado(lista.isEliminado());

            // Agregar las sucursales a la categoría DTO
            for (Sucursal sucursal : lista.getSucursales()) {
                SucursalSimpleDto sucursalDto = new SucursalSimpleDto();
                sucursalDto.setId(sucursal.getId());
                sucursalDto.setNombre(sucursal.getNombre());
                // Aquí puedes agregar otros campos de la entidad Sucursal al DTO si es necesario
                categoriadto.getSucursales().add(sucursalDto);
            }

            Set<Categoria> subCategorias = categoriaRepository.findByCategoriaPadre_Id(lista.getId());

            for (Categoria subCategoria : subCategorias) {
                SubCategoriaDto subCategoriaDto = agregarSubCategoriasRecursivamente(subCategoria);
                categoriadto.getSubCategoriaDtos().add(subCategoriaDto);
            }
            listaDto.add(categoriadto);
        }
        return listaDto;
    } catch (Exception e) {
        throw new Exception(e);
    }
}

    @Override
    public Categoria Actualizar(long id, Categoria categoria) throws Exception {
        try {
            if (!categoriaRepository.existsByIdAndEliminadoFalse(id)) {
                throw new Exception("No existe una categoria con ese id");
            }
            if (categoriaRepository.existsByDenominacionAndEliminadoFalse(categoria.getDenominacion())) {
                throw new Exception("Ya existe una categoria con esa denominacion");
            }
            Categoria categoriaActualizada = categoriaRepository.findByIdAndEliminadoFalse(id);

          categoriaActualizada.setDenominacion(categoria.getDenominacion());

            // Actualizar las sucursales
            List<Sucursal> sucursales = new ArrayList<>();
            for (Sucursal sucursaldto : categoria.getSucursales()) {
                Sucursal sucursal = sucursalRepository.findById(sucursaldto.getId())
                        .orElseThrow(() -> new Exception("No existe una sucursal con el id " + sucursaldto.getId()));
                sucursales.add(sucursal);
            }
            categoriaActualizada.setSucursales(sucursales);

            return categoriaRepository.save(categoriaActualizada);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

  @Override
public Set<CategoriaDto> traerCategoriaPadre(Long sucursalId) throws Exception {
    try {
        Set<Categoria> listaCategoriaOriginal = categoriaRepository.findBySucursales_IdAndEliminadoFalse(sucursalId);
        Set<CategoriaDto> listaDto = new HashSet<>();
        for (Categoria lista: listaCategoriaOriginal){
            CategoriaDto categoriadto = new CategoriaDto();
            categoriadto.setDenominacion(lista.getDenominacion());
            categoriadto.setUrlIcono(lista.getUrlIcono());
            categoriadto.setId(lista.getId());
            categoriadto.setEliminado(lista.isEliminado());

            listaDto.add(categoriadto);
        }
        return listaDto;

    } catch (Exception e) {
        throw new Exception(e);
    }
}


    // Obtengo las subcategorias
    public Set<SubCategoriaListaDto> obtenerSubCategorias(Long idCategoriaPadre) throws Exception {
        try {
            Set<Categoria> subCategorias = categoriaRepository.findByCategoriaPadre_Id(idCategoriaPadre);
            if (subCategorias.isEmpty()) {
                throw new Exception("No hay subcategorias para la categoria con id " + idCategoriaPadre);
            }
            Set<SubCategoriaListaDto> subcategoriaLista = new HashSet<>();
            for (Categoria s : subCategorias){
                SubCategoriaListaDto subcategoria = new SubCategoriaListaDto();
                subcategoria.setDenominacion(s.getDenominacion());
                subcategoria.setUrlIcono(s.getUrlIcono());
                subcategoria.setId(s.getId());
                subcategoriaLista.add(subcategoria);
            }
            return subcategoriaLista;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean tieneSubCategorias(Long categoriaId) throws Exception {
        try {
            return categoriaRepository.existsByCategoriaPadre_IdAndEliminadoFalse(categoriaId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

 private SubCategoriaDto agregarSubCategoriasRecursivamente(Categoria categoria) {
    SubCategoriaDto subCategoriaDto = new SubCategoriaDto();
    subCategoriaDto.setDenominacion(categoria.getDenominacion());
    subCategoriaDto.setUrlIcono(categoria.getUrlIcono());
    subCategoriaDto.setId(categoria.getId());
    subCategoriaDto.setIdCategoriaPadre(categoria.getCategoriaPadre() != null ? categoria.getCategoriaPadre().getId() : null);
    subCategoriaDto.setEliminado(categoria.isEliminado());

     for (Sucursal sucursal : categoria.getSucursales()) {
         SucursalSimpleDto sucursalDto = new SucursalSimpleDto();
         sucursalDto.setId(sucursal.getId());
         sucursalDto.setNombre(sucursal.getNombre());
         // Establece aquí otros campos que necesites de la Sucursal
         subCategoriaDto.getSucursales().add(sucursalDto);
     }

    Set<Categoria> subCategorias = categoriaRepository.findByCategoriaPadre_Id(categoria.getId());
    for (Categoria subCategoria : subCategorias) {
        SubCategoriaDto subSubCategoriaDto = agregarSubCategoriasRecursivamente(subCategoria);
        subCategoriaDto.getSubSubCategoriaDtos().add(subSubCategoriaDto);
    }

    return subCategoriaDto;
}
//----------------------------------- Categoria con Empresas--------------------
    @Autowired
    private IEmpresaRepository empresaRepository;

    public Categoria crearCategoriaporEmpresa(CategoriaEmpresaDTO categoriaEmpresaDTO) {
    Empresa empresa = empresaRepository.findById(categoriaEmpresaDTO.getEmpresaId())
            .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

    Categoria categoria = new Categoria();
    categoria.setDenominacion(categoriaEmpresaDTO.getDenominacion());
    categoria.setEmpresa(empresa);

    return categoriaRepository.save(categoria);
}

    public Categoria actualizarDenominacion(Long id, String nuevaDenominacion) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        categoria.setDenominacion(nuevaDenominacion);

        return categoriaRepository.save(categoria);
    }

    public Categoria cambiarEstadoEliminado(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        categoria.setEliminado(!categoria.isEliminado());

        return categoriaRepository.save(categoria);
    }

    public Categoria crearSubCategoriaConEmpresa(SubCategoriaConEmpresaDTO subCategoriaDTO) {
        Categoria categoriaPadre = categoriaRepository.findById(subCategoriaDTO.getIdCategoriaPadre())
                .orElseThrow(() -> new IllegalArgumentException("Categoría padre no encontrada"));

        // Verificar si la empresa de la categoría padre es nula
        if (categoriaPadre.getEmpresa() == null) {
            throw new IllegalArgumentException("La categoría padre no tiene una empresa asociada");
        }

        // Obtener el ID de la empresa de la categoría padre
        Long idEmpresaCategoriaPadre = categoriaPadre.getEmpresa().getId();

        Categoria subCategoria = new Categoria();
        subCategoria.setDenominacion(subCategoriaDTO.getDenominacion());
        subCategoria.setCategoriaPadre(categoriaPadre);

        // Establecer la empresa de la categoría padre en la subcategoría
        subCategoria.setEmpresa(categoriaPadre.getEmpresa());

        categoriaPadre.agregarSubCategoria(subCategoria);

        // Guardar el ID de la empresa de la categoría padre en el DTO
        subCategoriaDTO.setIdEmpresaCategoriaPadre(idEmpresaCategoriaPadre);

        return categoriaRepository.save(subCategoria);
    }
   //---------------------


    private CategoriaEmpresaDTO convertirACategoriaEmpresaDTO(Categoria categoria, int depth, Set<Long> procesadas) {
        if (procesadas.contains(categoria.getId())) {
            return null; // ya procesado
        }
        procesadas.add(categoria.getId());

        CategoriaEmpresaDTO dto = new CategoriaEmpresaDTO();
        dto.setId(categoria.getId());
        dto.setDenominacion(categoria.getDenominacion());
        dto.setEmpresaId(categoria.getEmpresa().getId());
        dto.setEliminado(categoria.isEliminado()); // Asegúrate de usar el getter correcto

        if (depth > 0) {
            Set<SubCategoriaConEmpresaDTO> subCategoriaDtos = categoria.getSubCategorias().stream()
                    .map(subCategoria -> convertirASubCategoriaConEmpresaDTO(subCategoria, depth - 1, procesadas))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            dto.setSubCategoriaDtos(subCategoriaDtos);
        }

        return dto;
    }

    private SubCategoriaConEmpresaDTO convertirASubCategoriaConEmpresaDTO(Categoria subCategoria, int depth, Set<Long> procesadas) {
        if (procesadas.contains(subCategoria.getId())) {
            return null; // ya procesado
        }
        procesadas.add(subCategoria.getId());

        SubCategoriaConEmpresaDTO dto = new SubCategoriaConEmpresaDTO();
        dto.setId(subCategoria.getId());
        dto.setDenominacion(subCategoria.getDenominacion());
        dto.setIdCategoriaPadre(subCategoria.getCategoriaPadre().getId());
        dto.setIdEmpresaCategoriaPadre(subCategoria.getCategoriaPadre().getEmpresa().getId());
        dto.setEliminado(subCategoria.isEliminado()); // Asegúrate de usar el getter correcto

        if (depth > 0) {
            Set<SubCategoriaConEmpresaDTO> subSubCategoriaDtos = subCategoria.getSubCategorias().stream()
                    .map(subSubCategoria -> convertirASubCategoriaConEmpresaDTO(subSubCategoria, depth - 1, procesadas))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            dto.setSubSubCategoriaDtos(subSubCategoriaDtos);
        }

        return dto;
    }
//----------------------
public Set<CategoriaDto> traerTodo2(Long empresaId) throws Exception {
    try {
        Set<Categoria> listaCategoriaOriginal = categoriaRepository.findByEmpresaId(empresaId);
        Set<CategoriaDto> listaDto = new HashSet<>();
        for (Categoria lista: listaCategoriaOriginal){
            // Solo agregar a la lista las categorías que no tienen una categoría padre
            if (lista.getCategoriaPadre() == null) {
                CategoriaDto categoriadto = new CategoriaDto();
                categoriadto.setDenominacion(lista.getDenominacion());
                categoriadto.setUrlIcono(lista.getUrlIcono());
                categoriadto.setId(lista.getId());
                categoriadto.setEliminado(lista.isEliminado());

                Set<Categoria> subCategorias = categoriaRepository.findByCategoriaPadre_Id(lista.getId());

                for (Categoria subCategoria : subCategorias) {
                    if (subCategoria.getCategoriaPadre() != null && subCategoria.getCategoriaPadre().getId().equals(lista.getId())) {
                        SubCategoriaDto subCategoriaDto = agregarSubCategoriasRecursivamente2(subCategoria, empresaId);
                        categoriadto.getSubCategoriaDtos().add(subCategoriaDto);
                    }
                }
                listaDto.add(categoriadto);
            }
        }
        return listaDto;
    } catch (Exception e) {
        throw new Exception(e);
    }
}

    private SubCategoriaDto agregarSubCategoriasRecursivamente2(Categoria categoria, Long empresaId) {
        SubCategoriaDto subCategoriaDto = new SubCategoriaDto();
        subCategoriaDto.setDenominacion(categoria.getDenominacion());
        subCategoriaDto.setUrlIcono(categoria.getUrlIcono());
        subCategoriaDto.setId(categoria.getId());
        subCategoriaDto.setIdCategoriaPadre(categoria.getCategoriaPadre() != null ? categoria.getCategoriaPadre().getId() : null);
        subCategoriaDto.setEliminado(categoria.isEliminado());

        Set<Categoria> subCategorias = categoriaRepository.findByCategoriaPadre_IdAndEmpresa_Id(categoria.getId(), empresaId);
        for (Categoria subCategoria : subCategorias) {
            if (subCategoria.getCategoriaPadre() != null && subCategoria.getCategoriaPadre().getId().equals(categoria.getId())) {
                SubCategoriaDto subSubCategoriaDto = agregarSubCategoriasRecursivamente2(subCategoria, empresaId);
                subCategoriaDto.getSubSubCategoriaDtos().add(subSubCategoriaDto);
            }
        }
        return subCategoriaDto;
    }



}
