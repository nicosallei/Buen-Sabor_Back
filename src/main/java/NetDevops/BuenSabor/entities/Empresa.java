package NetDevops.BuenSabor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Empresa extends Base{
private String nombre;
private String razonSocial;
private Long cuil;
private String imagen;




}
