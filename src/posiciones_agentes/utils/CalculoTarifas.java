package posiciones_agentes.utils;

import com.jfoenix.controls.JFXDatePicker;
import posiciones_agentes.models.RegistroPosicionesAgentes;
import posiciones_agentes.models.TarifasPosicionAgente;
import services.ServiceLocator;
import util.ConfigProperties;
import util.Util;

import java.sql.Date;
import java.time.LocalDate;

/**
 * @author Humberto Cabrera Dominguez
 * @implSpec  In this class are the methods to calculate the Tarifas by provider of service
 * @util Utils
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
                int festiveDays = festiveDays(Util.festivosKeys[pos]);
                int cantEfect = posicionesAgentes.getCantidadEfectivos();
                sum += tarifa.getTarifa()*(190.6*cantEfect+24*festiveDays);
                pos++;
            }
        }else{
            int pos =0;
            while(pos<Util.meses.length){
                int worklyDays = calculateWorklyDays(year,pos);
                int notWorklyDays = calculateNotWroklyDays(Util.noLaborablesKeys[pos]);
                int horasWorklyDays = posicionesAgentes.getHorasDiasLaborables();
                int horasNotWorklyDays = posicionesAgentes.getHorasDiasNoLaborables();
                sum+= tarifa.getTarifa()*(worklyDays*horasWorklyDays+notWorklyDays*horasNotWorklyDays);
                pos++;
            }
        }
        return sum;
    }


    private static int calculateWorklyDays(int year,int month){
        int cantDias = 0;
        switch (month){
            case 0: case 2: case 4: case 6: case 7: case 9: case 11:
                cantDias = 31-calculateNotWroklyDays(Util.noLaborablesKeys[month]);
                break;
            case 3: case 5: case 8: case 10:
                cantDias= 30-calculateNotWroklyDays(Util.noLaborablesKeys[month]);
                break;
            case 1:
                cantDias= year%400==0?29-calculateNotWroklyDays(Util.noLaborablesKeys[month]):28-calculateNotWroklyDays(Util.noLaborablesKeys[month]);
                break;
        }
        return cantDias;
    }

    private static int calculateNotWroklyDays(String month){
        return Integer.parseInt(ConfigProperties.getProperties().getProperty(month));
    }

    private static int festiveDays(String month){
        return Integer.parseInt(ConfigProperties.getProperties().getProperty(month));
    }



}
