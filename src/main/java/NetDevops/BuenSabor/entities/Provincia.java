package NetDevops.BuenSabor.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Provincia extends Base {
    private String nombre;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Pais pais;
}
