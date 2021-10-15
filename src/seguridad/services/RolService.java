package seguridad.services;

import seguridad.models.Rol;

import java.util.List;

public interface RolService {
    List<Rol> getAllRols();
    Rol getRolById(int id);
    Rol getRolByName(String nombre);
    int saveRol(Rol rol);
    void deleteRolById(int id);
    int updateRol(Rol rol);
}
