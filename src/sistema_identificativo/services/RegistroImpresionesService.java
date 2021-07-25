package sistema_identificativo.services;

import sistema_identificativo.models.RegistroImpresiones;

import java.util.List;

public interface RegistroImpresionesService {

    List<RegistroImpresiones> getAllRegistroImpresiones();

    RegistroImpresiones getRegistroImpresionesByIdReg(int idReg);

    RegistroImpresiones getRegistroImpresionesById(int id);

    void deleteRegistroImpresionesById(int id);

    int updateRegistroImpresiones(RegistroImpresiones registroImpresiones);

    int saveRegistroImpresiones(RegistroImpresiones registroImpresiones);

}
