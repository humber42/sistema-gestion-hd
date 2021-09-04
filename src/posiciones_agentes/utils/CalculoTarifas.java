package posiciones_agentes.utils;

import posiciones_agentes.models.RegistroPosicionesAgentes;
import posiciones_agentes.models.TarifasPosicionAgente;
import services.ServiceLocator;
import util.Util;

/**
 * @author Humberto Cabrera Dominguez
 * @implSpec  In this class are the methods to calculate the Tarifas by provider of service
 */
public class CalculoTarifas {
    /**
     * Unified method to calculate all the amount that cost the service on the Position
     * @param posicionesAgentes Is the position where the agent is located
     * @param year Year
     * @return Total cost of that position on the year
     */
    public static double calculateByProviderOnAYear(RegistroPosicionesAgentes posicionesAgentes, int year){
        double sum = 0;
        TarifasPosicionAgente tarifa = ServiceLocator.getTarifaPosicionAgenteService()
                .getTarifaByUoAndProv(posicionesAgentes.getUnidadOrganizativa().getId_unidad_organizativa()
                        ,posicionesAgentes.getProveedorServicio().getId());

        if(posicionesAgentes.getProveedorServicio().getProveedorServicio().equalsIgnoreCase("SEPCOM")){
            int pos =0;
            while(pos<Util.meses.length){
                int festiveDays = festiveDays(Util.meses[pos]);
                int cantEfect = posicionesAgentes.getCantidadEfectivos();
                sum += tarifa.getTarifa()*(190.6*cantEfect+24*festiveDays);
                pos++;
            }
        }else{
            int pos =0;
            while(pos<Util.meses.length){
                int worklyDays = calculateWorklyDays(year,Util.meses[pos]);
                int notWorklyDays = calculateNotWroklyDays(year,Util.meses[pos]);
                int horasWorklyDays = posicionesAgentes.getHorasDiasLaborables();
                int horasNotWorklyDays = posicionesAgentes.getHorasDiasNoLaborables();
                sum+= tarifa.getTarifa()*(worklyDays*horasWorklyDays+notWorklyDays*horasNotWorklyDays);
                pos++;
            }
        }
        return sum;
    }


    private static int calculateWorklyDays(int year,String month){
        return Integer.MIN_VALUE;
    }

    private static int calculateNotWroklyDays(int year,String month){
        return Integer.MIN_VALUE;
    }

    private static int festiveDays(String month){
        return Integer.MIN_VALUE;
    }



}
