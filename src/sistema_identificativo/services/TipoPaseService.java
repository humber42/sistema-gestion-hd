package sistema_identificativo.services;

import sistema_identificativo.models.TipoPase;

import java.util.List;

public interface TipoPaseService {

    List<TipoPase> getAllTipoPase();

    TipoPase getTipoPaseById(int id);

    void deleteTipoPaseById(int id);

    int saveTipoPase(TipoPase tipoPase);


}
