package NetDevops.BuenSabor.entities;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
//@Audited
public class ImagenPromocion extends Base{
    private String url;
}
