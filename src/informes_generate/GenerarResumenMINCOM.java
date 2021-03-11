package informes_generate;

import models.Hechos;

import java.util.LinkedList;

public interface GenerarResumenMINCOM {

    boolean generarResumenMINCOM(LinkedList<Hechos> hechos);

}
