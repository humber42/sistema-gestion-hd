package sistema_identificativo.services.impl;

import services.ServiceLocator;
import sistema_identificativo.models.RegistroPase;
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
    public int addPictureToRegistroPase(String imagen,int idRegistro) throws SQLException {
        var function = "{call add_picture_registro_pase(?,?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1,idRegistro);
        statement.setString(2,imagen);
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

//    @Override
//    public int updateRegistroPase(RegistroPase registroPase) throws SQLException{
//        return 0;
//    }

    @Override
    public List<RegistroPase> getAllRegistroPase() {
        List<RegistroPase> registroPaseList = new LinkedList<>();
        try{
            var query = "Select * From registro_pases";
            registroPaseList = recuperarLista(Util.executeQuery(query));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return registroPaseList;
    }

    @Override
    public RegistroPase getRegistroPaseById(int id) {
        RegistroPase registroPase = new RegistroPase();
        try{
            var query = "Select * From id_reg="+id;
            registroPase = this.recuperarRegistroPase(Util.executeQuery(query));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return registroPase;
    }

    @Override
    public void deleteRegistroPase(int id) throws SQLException{
        var function = "{call delete_registro_pase(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1,id);
        statement.execute();
        statement.close();
    }

    //Recuperar results sets

    private LinkedList<RegistroPase> recuperarLista(ResultSet set) throws SQLException{
        LinkedList<RegistroPase> registroPaseLinkedList = new LinkedList<>();
        while(set.next()){
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
                            set.getString(11)
                    )
            );
        }
        return registroPaseLinkedList;
    }

    private RegistroPase recuperarRegistroPase(ResultSet set) throws SQLException{
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
                set.getString(11));
    }

}
