package NetDevops.BuenSabor.dto.usuario;

import NetDevops.BuenSabor.dto.BaseDto;
import NetDevops.BuenSabor.entities.Cliente;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistroDto extends BaseDto {
    private String username;
    private String password;
    private Cliente cliente;
}
