package sistema_identificativo.services;

import sistema_identificativo.models.CodigoPase;

import java.sql.SQLException;
import java.util.List;

public interface CodigoPaseService {

    List<CodigoPase> getAllCodigo();

    CodigoPase getCodigoPaseById(int id);

    void deleteCodigoPase(int id) throws SQLException;

    int saveCodigoPase(CodigoPase codigoPase) throws SQLException;

    CodigoPase getCodigoByName(String name);

}
