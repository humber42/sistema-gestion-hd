package services;

import models.EstacionPublicaCentroAgente;

import java.util.List;

public interface EstacionPublicaCentroAgenteService {
    int countAll();
    List<EstacionPublicaCentroAgente> getAll();
    List<EstacionPublicaCentroAgente> getAllByIdUORG(int id_uorg);
    EstacionPublicaCentroAgente getEstacionPublicaById(int id);
    void addEstacionPublicaCentroAgente(EstacionPublicaCentroAgente estacion);
    void updateEstacionPublicaByIdMunicipio(int id_municipio, int centros, int estaciones);
    void updateEstacionPublicaCentroAgente(EstacionPublicaCentroAgente estacion);
    void deleteEstacionPublicaCentroAgente(int id);
}
