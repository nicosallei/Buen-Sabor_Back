package NetDevops.BuenSabor.service.impl;

import NetDevops.BuenSabor.entities.Localidad;
import NetDevops.BuenSabor.repository.ILocalidadRepository;
import NetDevops.BuenSabor.service.ILocalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalidadService implements ILocalidadService {
    @Autowired
    private ILocalidadRepository localidadRepository;


    @Override
    public Boolean eliminar(Long id) throws Exception {
        try {
            Localidad localidad = localidadRepository.findById(id).get();
            localidad.setEliminado(true);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Localidad guardar(Localidad entity) throws Exception {
        try {
            return localidadRepository.save(entity);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Localidad modificar(Long id, Localidad entity) throws Exception {
        try {
            Localidad localidad = localidadRepository.findById(id).get();
            localidad.setNombre(entity.getNombre());
            return localidadRepository.save(localidad);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Localidad buscarPorId(Long id) throws Exception {
        try {
            return localidadRepository.findById(id).get();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Localidad> buscarTodos() throws Exception {
        try {
            return localidadRepository.findAll();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Boolean reactivar(Long id) throws Exception {
        try {
            Localidad localidad = localidadRepository.findById(id).get();
            localidad.setEliminado(false);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
