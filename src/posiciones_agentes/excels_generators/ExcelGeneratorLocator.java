package posiciones_agentes.excels_generators;

import posiciones_agentes.excels_generators.impl.ResumenGeneralGeneratorImpl;

public class ExcelGeneratorLocator {

    private static ResumenGeneralGenerator resumenGeneralGenerator = null;

    public static ResumenGeneralGenerator getResumenGeneralGenerator() {
        if(resumenGeneralGenerator==null){
            resumenGeneralGenerator = new ResumenGeneralGeneratorImpl();
        }
        return resumenGeneralGenerator;
    }
}
