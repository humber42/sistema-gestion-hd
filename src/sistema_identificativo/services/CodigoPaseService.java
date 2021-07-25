package sistema_identificativo.services;

import sistema_identificativo.models.CodigoPase;

import java.util.List;

public interface CodigoPaseService {

    List<CodigoPase> getAllCodigo();

    CodigoPase getCodigoPaseById(int id);

    void deleteCodigoPase(int id);

    int saveCodigoPase(CodigoPase codigoPase);
}
