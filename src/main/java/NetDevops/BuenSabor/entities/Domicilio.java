package NetDevops.BuenSabor.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Domicilio extends Base{
    private String calle;
    private Integer numero;
    private Integer cp;

    @ManyToOne()
    private Localidad localidad;

    @ManyToMany(mappedBy = "domicilios")
    private List<Cliente> clientes;
}
