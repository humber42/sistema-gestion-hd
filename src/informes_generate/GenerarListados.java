package informes_generate;

import models.Hechos;

import java.util.LinkedList;

public interface GenerarListados {

    boolean generarListado(LinkedList<Hechos> hechos, String fileAddres);

    boolean generarListado(LinkedList<Hechos> hechos, String fileAddres, int tipoHechos);
}
