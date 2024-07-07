package NetDevops.BuenSabor.entities;

import NetDevops.BuenSabor.enums.Rol;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Cliente extends Base{

    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private LocalDate fechaNacimiento;
    private Rol rol = Rol.CLIENTE;
    private String imagen;


   @ManyToMany
    @JoinTable(
  name = "cliente_domicilio",
  joinColumns = @JoinColumn(name = "cliente_id"),
  inverseJoinColumns = @JoinColumn(name = "domicilio_id"))
private List<Domicilio> domicilios;

    @OneToOne
    private UsuarioCliente usuarioCliente;

}
