package services;

import models.Anno;

import java.util.List;

public interface AnnoService {

    public Anno searchAnno(int id_anno);

    public Anno getActiveAnno();

    public List<Anno> allAnno();


}
