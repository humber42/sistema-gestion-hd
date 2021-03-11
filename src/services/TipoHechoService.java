package services;

import models.TipoHecho;

import java.util.List;

public interface TipoHechoService {


    TipoHecho getOneTipoHecho(int id);

    List<TipoHecho> fetchAll();

    TipoHecho searchTipoHechoByName(String name);

}
