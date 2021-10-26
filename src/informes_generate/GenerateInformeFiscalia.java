package informes_generate;

import java.time.LocalDate;

public interface GenerateInformeFiscalia {

    boolean generarInformeCompleto(LocalDate localDate, String path);


}
