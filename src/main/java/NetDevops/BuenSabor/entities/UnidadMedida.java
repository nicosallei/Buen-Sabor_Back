package NetDevops.BuenSabor.entities;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UnidadMedida extends Base{
    private String denominacion;
}
