package services;

import models.TipoHecho;

import java.util.List;

public interface TipoHechoService {


    TipoHecho getOneTipoHecho(int id);

    List<TipoHecho> fetchAll();

    TipoHecho getTipoHechoOfHechoByIdReg(int idReg);

    TipoHecho searchTipoHechoByName(String name);

}
