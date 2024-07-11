package NetDevops.BuenSabor.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
//@Audited
public class Localidad extends Base {
    private String nombre;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Provincia provincia;
}
