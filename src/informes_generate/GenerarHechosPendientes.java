package informes_generate;

import models.Hechos;

import java.util.LinkedList;

public interface GenerarHechosPendientes {

    boolean generarHechosPendientes(LinkedList<Hechos> pendientes, String path);
}
