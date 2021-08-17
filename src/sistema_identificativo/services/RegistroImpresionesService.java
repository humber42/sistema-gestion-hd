package sistema_identificativo.services;

import sistema_identificativo.models.RegistroImpresiones;

import java.sql.SQLException;
import java.util.List;

public interface RegistroImpresionesService {

    List<RegistroImpresiones> getAllRegistroImpresiones();

    RegistroImpresiones getRegistroImpresionesByIdReg(int idReg);

    RegistroImpresiones getRegistroImpresionesById(int id);

    void deleteRegistroImpresionesById(int id) throws SQLException;

    int updateRegistroImpresiones(RegistroImpresiones registroImpresiones) throws SQLException;

    int saveRegistroImpresiones(RegistroImpresiones registroImpresiones) throws SQLException;

}
