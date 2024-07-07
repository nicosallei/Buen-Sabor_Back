package NetDevops.BuenSabor.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
//@Audited
public class ImagenCliente extends Base{
    private String url;
}
