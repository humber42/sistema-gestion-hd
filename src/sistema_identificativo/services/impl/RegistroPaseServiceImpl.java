package sistema_identificativo.services.impl;

import services.ServiceLocator;
import sistema_identificativo.models.RegistroPase;

import sistema_identificativo.models.CodigoPase;
import sistema_identificativo.models.RegistroPase;
import sistema_identificativo.models.TipoPase;

import sistema_identificativo.services.RegistroPaseService;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RegistroPaseServiceImpl implements RegistroPaseService {

    @Override
    public int addPictureToRegistroPase(String imagen, int idRegistro) throws SQLException {
        var function = "{call add_picture_registro_pase(?,?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, idRegistro);
        statement.setString(2, imagen);
        statement.execute();
        statement.close();
        return 0;
    }

    @Override

    public int saveRegistroPase(RegistroPase registroPase) throws SQLException{
        var function = "{call registrar_pase(?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1,registroPase.getTipoPase().getId());
        statement.setInt(2,registroPase.getCodigoPase().getId());
        statement.setString(3,registroPase.getNumeroPase());
        statement.setString(4,registroPase.getNumeroIdentidad());
        statement.setString(5,registroPase.getNombre());
        statement.setInt(6,registroPase.getUnidadOrganizativa().getId_unidad_organizativa());
        statement.setString(7,registroPase.getAcceso());
        statement.setDate(8,registroPase.getFechaValidez());
        statement.setInt(9,registroPase.getBaja());
        statement.setString(10,registroPase.getObservaciones());
        statement.execute();
        statement.close();
        return 0;
    }


    public String ultimoRegisroPase(String tipoPase, String codigoPase) {
        TipoPase tipoPaseObject = ServiceLocator.getTipoPaseService()
                .getTipoPaseByName(tipoPase);
        CodigoPase codigoPaseObject = ServiceLocator.getCodigoPaseService()
                .getCodigoByName(codigoPase);
        String numeroPase = "";

        var query = "Select * from obtener_mayor_numero_pase(" + tipoPaseObject.getId() + "," + codigoPaseObject.getId() + ")";
        try {
            ResultSet set = Util.executeQuery(query);
            if (set.next()) {
                numeroPase = set.getString(1);
                System.out.println(numeroPase);
                numeroPase = String.valueOf(Integer.valueOf(numeroPase) + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } catch (NumberFormatException e) {
            numeroPase = "0001";
        }
        return numeroPase;
    }

    @Override
    public int saveRegistroPase(RegistroPase registroPase) throws SQLException {
        var function = "{call crear_pase_whithout_date(?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, registroPase.getTipoPase().getId());// id_tipo_pase integer,
        statement.setInt(2, registroPase.getCodigoPase().getId());//id_codigo_pase integer
        statement.setString(3, registroPase.getNumeroPase());//numero_pase character varying
        statement.setString(4, registroPase.getNumeroIdentidad());//numero_identidad character varying
        statement.setString(5, registroPase.getNombre());//nombre character varying
        statement.setInt(6, registroPase.getUnidadOrganizativa().getId_unidad_organizativa());//id_unidad_organizativa integer
        statement.setString(7, registroPase.getAcceso());//acceso character varying,
//        String fecha = registroPase.getFechaValidez().toString();
//        System.out.println(fecha);
        //statement.setDate(8, Date.valueOf(fecha));//fecha_validez date
        statement.setInt(8, registroPase.getBaja());//baja integer
        statement.setString(9, registroPase.getObservaciones());//observaciones character varying
        statement.setString(10, registroPase.getImageUrl());//image character varying
        statement.execute();
        statement.close();

        if (registroPase.getFechaValidez() != null) {
            actualizarFecha(registroPase);
        }

        return 0;
    }

    /**
     * This method is to add the date to a
     * pass because CallableStatement doesn't understand the previous function
     *
     * @param registroPase Object Register pass that gonna be register into database
     * @throws SQLException Exception throw by PostgreSql Server
     */
    private void actualizarFecha(RegistroPase registroPase) throws SQLException {
        var function = "{call add_date_to_pass_register(?,?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setString(1, registroPase.getNumeroIdentidad());
        statement.setDate(2, registroPase.getFechaValidez());
        statement.execute();
        statement.close();
    }

//    @Override
//    public int updateRegistroPase(RegistroPase registroPase) throws SQLException{
//        return 0;
//    }

    @Override
    public List<RegistroPase> getAllRegistroPase() {
        List<RegistroPase> registroPaseList = new LinkedList<>();
        try {
            var query = "Select * From registro_pases";
            registroPaseList = recuperarLista(Util.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registroPaseList;
    }

    @Override
    public RegistroPase getRegistroPaseById(int id) {
        RegistroPase registroPase = new RegistroPase();
        try {
            var query = "Select * From id_reg=" + id;
            registroPase = this.recuperarRegistroPase(Util.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registroPase;
    }

    @Override
    public void deleteRegistroPase(int id) throws SQLException {
        var function = "{call delete_registro_pase(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, id);
        statement.execute();
        statement.close();
    }

    
    @Override
    public List<String> pasesPendientesFoto() {
        List<String> pasesName = new LinkedList<>();
        try {
            var query = "Select nombre from registro_pases where registro_pases.image_url=''";
            ResultSet resultSet = Util.executeQuery(query);
            while (resultSet.next()) {
                pasesName.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pasesName;
    }

    @Override
    public RegistroPase getPaseByPassName(String passName) {
        RegistroPase pase = new RegistroPase();
        try {
            var query = "Select * from registro_pases where nombre='" + passName + "'";
            pase = recuperarRegistroPase(Util.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pase;
    }

    //Recuperar results sets

    private LinkedList<RegistroPase> recuperarLista(ResultSet set) throws SQLException {
        LinkedList<RegistroPase> registroPaseLinkedList = new LinkedList<>();
        while (set.next()) {
            registroPaseLinkedList.add(
                    new RegistroPase(
                            set.getInt(1),
                            ServiceLocator.getTipoPaseService()
                                    .getTipoPaseById(set.getInt(2)),
                            ServiceLocator.getCodigoPaseService()
                                    .getCodigoPaseById(set.getInt(3)),
                            set.getString(4),
                            set.getString(5),
                            set.getString(6),
                            ServiceLocator.getUnidadOrganizativaService()
                                    .getOneUnidadOrganizativa(set.getInt(7)),
                            set.getString(8),
                            set.getDate(9),
                            set.getInt(10),
                            set.getString(11),
                            set.getString(12)
                    )
            );
        }
        return registroPaseLinkedList;
    }

    private RegistroPase recuperarRegistroPase(ResultSet set) throws SQLException {
        set.next();

        return new RegistroPase(
                set.getInt(1),
                ServiceLocator.getTipoPaseService()
                        .getTipoPaseById(set.getInt(2)),
                ServiceLocator.getCodigoPaseService()
                        .getCodigoPaseById(set.getInt(3)),
                set.getString(4),
                set.getString(5),
                set.getString(6),
                ServiceLocator.getUnidadOrganizativaService()
                        .getOneUnidadOrganizativa(set.getInt(7)),
                set.getString(8),
                set.getDate(9),
                set.getInt(10),

                set.getString(11),
                set.getString(12)
        );

    }

}
