package sistema_identificativo.services;

import sistema_identificativo.models.RegistroPase;

import java.util.List;

public interface RegistroPaseService {

    List<RegistroPase> getAllRegistroPase();

    RegistroPase getRegistroPaseById(int id);

    void deleteRegistroPase(int id);

    int saveRegistroPase(RegistroPase regitroPase);

    int updateRegistroPase(RegistroPase registroPase);

    int addPictureToRegistroPase(String imagen);
}
