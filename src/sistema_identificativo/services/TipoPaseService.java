package sistema_identificativo.services;

import sistema_identificativo.models.TipoPase;

import java.sql.SQLException;
import java.util.List;

public interface TipoPaseService {

    List<TipoPase> getAllTipoPase();

    TipoPase getTipoPaseById(int id);

    void deleteTipoPaseById(int id) throws SQLException;

    int saveTipoPase(TipoPase tipoPase) throws SQLException;

    TipoPase getTipoPaseByName(String name);


}
