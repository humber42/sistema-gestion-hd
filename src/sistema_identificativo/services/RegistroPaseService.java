package sistema_identificativo.services;

import sistema_identificativo.models.RegistroPase;

import java.sql.SQLException;
import java.util.List;

public interface RegistroPaseService {

    List<RegistroPase> getAllRegistroPase();

    RegistroPase getRegistroPaseById(int id);


    void deleteRegistroPase(int id) throws SQLException;

    int saveRegistroPase(RegistroPase regitroPase) throws SQLException;

//    int updateRegistroPase(RegistroPase registroPase) throws SQLException;

    int addPictureToRegistroPase(String imagen,int idRegistro) throws SQLException;

    String ultimoRegisroPase(String tipoPase, String codigoPase);

    void deleteRegistroPase(int id) throws SQLException;

    int saveRegistroPase(RegistroPase regitroPase) throws SQLException;

//    int updateRegistroPase(RegistroPase registroPase) throws SQLException;

    int addPictureToRegistroPase(String imagen, int idRegistro) throws SQLException;

    List<String> pasesPendientesFoto();

    RegistroPase getPaseByPassName(String passName);

}
