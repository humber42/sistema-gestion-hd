package posiciones_agentes.excels_generators;

import posiciones_agentes.excels_generators.impl.ResumenGeneralGeneratorImpl;
import posiciones_agentes.excels_generators.impl.ResumenProveedorServicioImpl;
import posiciones_agentes.excels_generators.impl.ResumenUnidadOrganizativaImpl;

/**
 * @author Htico
 * @service Singleton to generate excels
 */
public class ExcelGeneratorLocator {

    private static ResumenGeneralGenerator resumenGeneralGenerator = null;
    private static ResumenProveedorServicio resumenProveedorServicio = null;
    private static ResumenUnidadOrganizativa resumenUnidadOrganizativa = null;

    public static ResumenUnidadOrganizativa getResumenUnidadOrganizativa(){
        if(resumenUnidadOrganizativa == null){
            resumenUnidadOrganizativa = new ResumenUnidadOrganizativaImpl();
        }
        return resumenUnidadOrganizativa;
    }

    public static ResumenProveedorServicio getResumenProveedorServicio(){
        if(resumenProveedorServicio==null){
            resumenProveedorServicio = new ResumenProveedorServicioImpl();
        }
        return resumenProveedorServicio;
    }

    public static ResumenGeneralGenerator getResumenGeneralGenerator() {
        if(resumenGeneralGenerator==null){
            resumenGeneralGenerator = new ResumenGeneralGeneratorImpl();
        }
        return resumenGeneralGenerator;
    }
}
