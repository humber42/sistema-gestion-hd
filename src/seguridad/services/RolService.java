package seguridad.services;

import seguridad.models.Rol;

import java.util.List;

public interface RolService {
    List<Rol> getAllRols();
    Rol getRolById(int id);
    Rol getRolByName(String nombre);
    void saveRol(Rol rol);
    void deleteRolById(int id);
    void updateRol(Rol rol);
}
