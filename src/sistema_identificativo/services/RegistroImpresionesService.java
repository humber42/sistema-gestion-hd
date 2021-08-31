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

    int saveNuevoRegistroImpresion(int id_reg) throws SQLException;

    int updateLastImpressionAndQuanty(int id) throws SQLException;

    void execNewOrUpdateImpressionRegister(String CI) throws SQLException;

}
