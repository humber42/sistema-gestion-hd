package posiciones_agentes.utils;

import posiciones_agentes.models.*;
import services.ServiceLocator;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CalculoByOurg {
    public static List<UOrgPosiciones> obtenerUOrgByPosicionesList(){
        //Valor a devolver
        List<UOrgPosiciones> uOrgPosiciones = new LinkedList<>();
        //Listas a procesar
        List<String> unidades = ServiceLocator.getRegistroPosicionesAgentesService().getAllUorgNames();
        List<PosicionAgente> registroPosicionesAgentesList = ServiceLocator.getRegistroPosicionesAgentesService()
                .getAll();
        //Procesamiento
        unidades.forEach(unidad->{
            List<PosicionAgente> registroPosicionesAgentes = registroPosicionesAgentesList
                    .stream()
                    .filter(registro->registro.getUnidadOrganizativa().equalsIgnoreCase(unidad))
                    .collect(Collectors.toList());
            UOrgPosiciones posicion = new UOrgPosiciones();
            posicion.setUorg(unidad);
            posicion.setCantPosiciones(registroPosicionesAgentes.size());
            registroPosicionesAgentes.forEach(registro->{
               if(registro.getProveedor().equalsIgnoreCase("AGESP")){
                   posicion.setAgesp(posicion.getAgesp()+1);
               }else if(registro.getProveedor().equalsIgnoreCase("SEPSA")){
                   posicion.setSepsa(posicion.getSepsa()+1);
               }else if(registro.getProveedor().equalsIgnoreCase("SEPCOM")){
                   posicion.setSepcom(posicion.getSepcom()+1);
               }else if(registro.getProveedor().equalsIgnoreCase("CAP")){
                   posicion.setCap(posicion.getCap()+1);
               }else if(registro.getProveedor().equalsIgnoreCase("Otros")){
                   posicion.setOtros(posicion.getOtros()+1);
               }
                posicion.setGasto(posicion.getGasto()+CalculoTarifas.calculateByProviderOnAYear(registro, LocalDate.now().getYear()));
            });
            uOrgPosiciones.add(posicion);
        });

        return uOrgPosiciones;
    }

    public static List<ProveedorGasto> calculoProveedorGasto(){
        List<ProveedorGasto> proveedorGastoList = new LinkedList<>();
        //Obteniendo info
        List<ProveedorServicio> proveedores = ServiceLocator.getProveedorServicioService().getAllProveedorServicio();
        List<PosicionAgente> registroPosicionesAgentesList = ServiceLocator.getRegistroPosicionesAgentesService().getAll();

        //Procesamiento
        proveedores.forEach(proveedorServicio -> {
            ProveedorGasto proveedorGasto = new ProveedorGasto();
            proveedorGasto.setProveedor(proveedorServicio.getProveedorServicio());
            List<PosicionAgente> registroPosicionesAgentes =registroPosicionesAgentesList
                    .stream()
                    .filter(registro->registro.getProveedor().equalsIgnoreCase(proveedorServicio.getProveedorServicio()))
                    .collect(Collectors.toList());
            proveedorGasto.setCantPosiciones(registroPosicionesAgentes.size());
            registroPosicionesAgentes.forEach(registro->{
                proveedorGasto.setGasto(proveedorGasto.getGasto()+CalculoTarifas.calculateByProviderOnAYear(registro,LocalDate.now().getYear()));
            });
            proveedorGastoList.add(proveedorGasto);
        });

        return proveedorGastoList;
    }
}
