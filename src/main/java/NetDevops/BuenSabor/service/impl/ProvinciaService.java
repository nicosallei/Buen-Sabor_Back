package NetDevops.BuenSabor.service.impl;

import NetDevops.BuenSabor.entities.Provincia;
import NetDevops.BuenSabor.repository.IProvinciaRepository;
import NetDevops.BuenSabor.service.IProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinciaService implements IProvinciaService {
    @Autowired
    private IProvinciaRepository provinciaRepository;
    @Override
    public Boolean eliminar(Long id) throws Exception {
       try {
              Provincia provincia = provinciaRepository.findById(id).get();
              provincia.setEliminado(true);
              return true;
         } catch (Exception e) {
              throw new Exception(e.getMessage());
       }
    }

    @Override
    public Provincia guardar(Provincia entity) throws Exception {
        try {
            return provinciaRepository.save(entity);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Provincia modificar(Long id, Provincia entity) throws Exception {
        try {
            Provincia provincia = provinciaRepository.findById(id).get();
            provincia.setNombre(entity.getNombre());
            return provinciaRepository.save(provincia);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Provincia buscarPorId(Long id) throws Exception {
       try {
                return provinciaRepository.findById(id).get();
             } catch (Exception e) {
                throw new Exception(e.getMessage());

       }
    }

    @Override
    public List<Provincia> buscarTodos() throws Exception {
        try {
            return provinciaRepository.findAll();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Boolean reactivar(Long id) throws Exception {
       try {
                Provincia provincia = provinciaRepository.findById(id).get();
                provincia.setEliminado(false);
                return true;
             } catch (Exception e) {
                throw new Exception(e.getMessage());
       }
    }
}
