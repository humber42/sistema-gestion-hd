package sistema_identificativo.services.impl;

import sistema_identificativo.models.Impresion;
import sistema_identificativo.services.ImpresionService;
import util.Conexion;
import util.Util;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ImpresionServiceImpl implements ImpresionService {
    private static final String SELECT = "SELECT DISTINCT numero_pase, numero_identidad, nombre, tipo_pase, substring(codigo,0,2), cant_impresiones FROM registro_pases";
    private static final String JOIN = " LEFT JOIN registro_impresiones ON registro_pases.id_reg = registro_impresiones.id_reg\n" +
            "         JOIN tipos_pase_identificativo ON registro_pases.id_tipo_pase = tipos_pase_identificativo.id\n" +
            "         JOIN codigo_pase_identificativo ON registro_pases.id_codigo_pase = codigo_pase_identificativo.id";
    private static final String BAJA = " AND baja = 0";

    public List<Impresion> getAllImpressions(){
        List<Impresion> impresionList = new LinkedList<>();
        String function = "{call obtener_impresiones()}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while(rs.next()){
                impresionList.add(new Impresion(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6)
                ));
            }
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return impresionList;
    }

    public List<Impresion> getAllByPassType(int passType){
        List<Impresion> impresionList = new LinkedList<>();
        try{
            String query = SELECT + JOIN + " WHERE id_tipo_pase = "+passType + BAJA;
            impresionList = retrieveList(Util.executeQuery(query));
        } catch (SQLException e){
            e.printStackTrace();
        }
        return impresionList;
    }

    public List<Impresion> getAllByContainName(String name){
        List<Impresion> impresionList = new LinkedList<>();
        try{
            String query = SELECT + JOIN + " WHERE nombre LIKE '%"+name + "%'" + BAJA;
            impresionList = retrieveList(Util.executeQuery(query));
        } catch (SQLException e){
            e.printStackTrace();
        }
        return impresionList;
    }

    public List<Impresion> getAllByPassTypeAndContainName(int passType, String name){
        List<Impresion> impresionList = new LinkedList<>();
        try{
            String query = SELECT + JOIN + " WHERE id_tipo_pase = " + passType + " AND nombre LIKE '%"+ name + "%'"
                    + BAJA;
            impresionList = retrieveList(Util.executeQuery(query));
        } catch (SQLException e){
            e.printStackTrace();
        }
        return impresionList;
    }

    private Impresion retrieveObject(ResultSet rs) throws SQLException{
        return new Impresion(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getInt(6)
        );
    }

    private LinkedList<Impresion> retrieveList(ResultSet rs) throws SQLException{
        LinkedList<Impresion> impresionLinkedList = new LinkedList<>();
        while (rs.next()){
            impresionLinkedList.add(this.retrieveObject(rs));
        }
        return impresionLinkedList;
    }
}
