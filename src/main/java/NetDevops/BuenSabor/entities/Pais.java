package NetDevops.BuenSabor.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
//@Audited
public class Pais extends Base{
    private String nombre;
}
