package posiciones_agentes.services;

import posiciones_agentes.models.TarifasPosicionAgente;

import java.util.List;

/**
 * @service TarifaPosicionAgente
 * @author Humberto Cabrera Dominguez
 */
public interface TarifaPosicionAgenteService {
    TarifasPosicionAgente getAgenteById(int id);
    void registerTarifa(TarifasPosicionAgente tarifasPosicionAgente);
    void updateTarifa(TarifasPosicionAgente tarifasPosicionAgente);
    List<TarifasPosicionAgente> getAll();
    void deleteByID(int id);
}
