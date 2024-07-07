package NetDevops.BuenSabor.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

import javax.management.relation.Role;

@Entity
@Data
public class UsuarioCliente extends Base{
    @Column(unique = true)
    private String username;
    private String password;

}
