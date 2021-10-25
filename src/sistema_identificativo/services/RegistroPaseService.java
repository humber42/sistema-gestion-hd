package sistema_identificativo.services;

import sistema_identificativo.models.RegistroPase;

import java.sql.SQLException;
import java.util.List;

public interface RegistroPaseService {

    List<RegistroPase> getAllRegistroPase();

    List<String> getAllPendingPhotosByContainName(String name);

    RegistroPase getRegistroPaseById(int id);

    String ultimoRegisroPase(String tipoPase, String codigoPase);

    int addPictureToRegistroPase(String imagen, int idRegistro) throws SQLException;

    int saveRegistroPase(RegistroPase registroPase) throws SQLException;

    void deleteRegistroPase(int id) throws SQLException;

    List<String> pasesPendientesFoto();

    RegistroPase getPaseByPassName(String passName);

    RegistroPase getPaseByCI(String CI) throws SQLException;

    void updateSeleccionado(String identidad) throws SQLException;

    void deselectAllSelections() throws SQLException;
}
