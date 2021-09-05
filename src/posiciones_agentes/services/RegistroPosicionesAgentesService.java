package posiciones_agentes.services;

import posiciones_agentes.models.RegistroPosicionesAgentes;

import java.util.List;


/**
 * @service RegistroPosicinesServicios
 * @author Humberto Cabrera Dominguez
 */
public interface RegistroPosicionesAgentesService {
    RegistroPosicionesAgentes getByID(int id);
    void registerRegisterPosicionesAgentes(RegistroPosicionesAgentes registroPosicionesAgentes);
    void updateRegisterPosicionesAgentes(RegistroPosicionesAgentes registroPosicionesAgentes);
    void eliminarRegisterPosicionesAgentes(RegistroPosicionesAgentes registroPosicionesAgentes);
    List<RegistroPosicionesAgentes> getAllRegistroPosicionesAgentes();
    List<RegistroPosicionesAgentes> getAllRegistrosByUOrg(int idUorg);
    List<String> getAllUorgNames();
}
