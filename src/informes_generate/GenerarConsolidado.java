package informes_generate;

import java.time.LocalDate;

public interface GenerarConsolidado {
    boolean generarConsolidado(LocalDate date, String path);
}
