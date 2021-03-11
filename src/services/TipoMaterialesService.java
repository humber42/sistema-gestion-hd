package services;

import models.MaterialsFiscaliaModels;
import models.TipoMateriales;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public interface TipoMaterialesService {

    TipoMateriales getOneTipo(int id);

    LinkedList<MaterialsFiscaliaModels> materialesPorAnno(Date date);

    List<TipoMateriales> fetchAll();
}
