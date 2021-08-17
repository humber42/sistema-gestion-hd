package sistema_identificativo.services;

import sistema_identificativo.models.Impresion;

import java.util.List;

public interface ImpresionService {
    List<Impresion> getAllImpressions();
    List<Impresion> getAllByContainName(String name);
    List<Impresion> getAllByPassType(int passType);
    List<Impresion> getAllByPassTypeAndContainName(int passType, String name);
}
