package NetDevops.BuenSabor.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class UsuarioEmpleado extends Base{
    private String username;
    private String password;

    private Empleado empleado;
}
