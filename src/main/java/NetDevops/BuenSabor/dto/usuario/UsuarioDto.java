package NetDevops.BuenSabor.dto.usuario;

import NetDevops.BuenSabor.dto.BaseDto;
import lombok.Data;

@Data
public class UsuarioDto extends BaseDto {
    private String username;
    private String password;
}
