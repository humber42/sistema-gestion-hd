package informes_generate;

import java.util.Objects;

public class GeneradorLocator {

    private static GenerateInformeFiscalia generateInformeFiscalia = null;
    private static GenerarInformeCentroDireccion generarInformeCentroDireccion = null;
    private static GenerarCertificoHechos generarCertificoHechos = null;
    private static GenerarHechosParaUOrg generarHechosParaUOrg = null;
    private static GenerarResumenMINCOM generarResumenMINCOM = null;
    private static GenerarHechosPrevenidos generarHechosPrevenidos = null;
    private static GenerarHechosPendientes generarHechosPendientes = null;
    private static GenerarPrevencion generarPrevencion = null;
    private static GenerarListados generarListados = null;
    private static ExportarExcel exportarExcel = null;
    private static GenerarConsolidado generarConsolidado = null;
    public static GenerateTotalesTpub generateTotalesTpub = null;

    public static GenerateTotalesTpub getGenerateTotalesTpub() {
        if (generateTotalesTpub == null) {
            generateTotalesTpub = new GenerateTotalesTpub();
        }
        return generateTotalesTpub;
    }

    public static GenerarConsolidado getGenerarConsolidado() {
        if (generarConsolidado == null) {
            generarConsolidado = new GenerarConsolidadoImpl();
        }
        return generarConsolidado;
    }


    public static GenerateInformeFiscalia getGenerateInformeFiscalia() {
        if (generateInformeFiscalia == null) {
            generateInformeFiscalia = new GenerateInformeFiscaliaImpl();
        }
        return generateInformeFiscalia;
    }

    public static GenerarInformeCentroDireccion getGenerarInformeCentroDireccion() {
        if (generarInformeCentroDireccion == null) {
            generarInformeCentroDireccion = new GenerarInformeCentroDireccionImpl();
        }
        return generarInformeCentroDireccion;
    }

    public static GenerarCertificoHechos getGenerarCertificoHechos() {
        if (generarCertificoHechos == null) {
            generarCertificoHechos = new GenerarCertificoHechosImpl();
        }
        return generarCertificoHechos;
    }

    public static GenerarHechosParaUOrg getGenerarHechosParaUOrg() {
        if (generarHechosParaUOrg == null) {
            generarHechosParaUOrg = new GenerarHechosParaUorgImpl();
        }
        return generarHechosParaUOrg;
    }

    public static GenerarResumenMINCOM getGenerarResumenMINCOM() {
        if (generarResumenMINCOM == null) {
            generarResumenMINCOM = new GenerarResumenMINCOMImpl();
        }
        return generarResumenMINCOM;
    }

    public static GenerarHechosPrevenidos getGenerarHechosPrevenidos() {
        if (Objects.isNull(generarHechosPrevenidos)) {
            generarHechosPrevenidos = new GenerarHechosPrevenidosImpl();
        }
        return generarHechosPrevenidos;
    }

    public static GenerarHechosPendientes getGenerarHechosPendientes() {
        if (Objects.isNull(generarHechosPendientes)) {
            generarHechosPendientes = new GenerarHechosPendientesImpl();
        }
        return generarHechosPendientes;
    }

    public static GenerarPrevencion getGenerarPrevencion() {
        if (Objects.isNull(generarPrevencion)) {
            generarPrevencion = new GenerarPrevencionImpl();
        }
        return generarPrevencion;
    }

    public static GenerarListados getGenerarListados() {
        if (generarListados == null) {
            generarListados = new GenerarListadosImpl();
        }
        return generarListados;
    }

    public static ExportarExcel getExportarExcel() {
        if (exportarExcel == null) {
            exportarExcel = new ExportarExcel();
        }
        return exportarExcel;
    }

}
