package posiciones_agentes.services;

import posiciones_agentes.models.TarifasPosicionAgente;

import java.sql.SQLException;
import java.util.List;

/**
 * @service TarifaPosicionAgente
 * @author Humberto Cabrera Dominguez
 */
public interface TarifaPosicionAgenteService {
    TarifasPosicionAgente getTarifaById(int id);
    void registerTarifa(TarifasPosicionAgente tarifasPosicionAgente);
    void updateTarifa(TarifasPosicionAgente tarifasPosicionAgente);
    List<TarifasPosicionAgente> getAll();
    void deleteByID(int id);
    TarifasPosicionAgente getTarifaByUoAndProv(int id_uorg, int id_prov);
}
