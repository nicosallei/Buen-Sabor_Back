package NetDevops.BuenSabor.dto.usuario;

import NetDevops.BuenSabor.enums.Rol;
import lombok.Data;


@Data
public class UserResponseDto  {
    private String username;
    private Rol role;
    private Long idUsuario;
}
