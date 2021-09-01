package posiciones_agentes.services;

import posiciones_agentes.models.ProveedorServicio;

import java.sql.SQLException;
import java.util.List;

/**
 * @service ProveedorServicio
 * @author Humberto Cabrera Dominguez
 */
public interface ProveedorServicioService {
    ProveedorServicio getById(int id);
    List<ProveedorServicio> getAllProveedorServicio();
    void registerProveedorServicio(ProveedorServicio proveedorServicio);
    void updateProveedorServicio(ProveedorServicio proveedorServicio);
    void deleteProveedorServicioById(int id);
    ProveedorServicio getByName(String name);
}
