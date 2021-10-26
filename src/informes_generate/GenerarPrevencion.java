package informes_generate;

import java.sql.Date;

public interface GenerarPrevencion {
    boolean generarInformePrevencion(Date fechaInicio, Date fechaFin, String path);
}
