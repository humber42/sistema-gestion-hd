package services;

import models.EstacionPublicaCentroAgente;

import java.util.List;

public interface EstacionPublicaCentroAgenteService {
    int countAll();
    List<EstacionPublicaCentroAgente> getAll();
    List<EstacionPublicaCentroAgente> getAllByIdUORG(int id_uorg);
    EstacionPublicaCentroAgente getEstacionPublicaById(int id);
    void addEstacionPublicaCentroAgente(EstacionPublicaCentroAgente estacion);
    void updateEstacionPublicaCentroAgente(EstacionPublicaCentroAgente estacion);
    void deleteEstacionPublicaCentroAgente(int id);
}
