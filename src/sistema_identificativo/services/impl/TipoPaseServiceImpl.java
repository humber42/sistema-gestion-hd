package sistema_identificativo.services.impl;

import sistema_identificativo.models.TipoPase;
import sistema_identificativo.services.TipoPaseService;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class TipoPaseServiceImpl implements TipoPaseService {

    @Override
    public int saveTipoPase(TipoPase tipoPase) throws SQLException {
        String function = "{call add_tipo_pase(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setString(1, tipoPase.getTipoPase());
        statement.execute();
        statement.close();
        return 0;
    }

    @Override

    public TipoPase getTipoPaseByName(String name) {
        TipoPase tipoPase = new TipoPase();
        try {
            String query = "Select * FROM tipos_pase_identificativo WHERE tipo_pase='" + name + "'";
            ResultSet rs = Util.executeQuery(query);
            tipoPase = recuperarObjeto(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoPase;
    }

    @Override
    public List<TipoPase> getAllTipoPase() {
        List<TipoPase> tipoPaseList = new LinkedList<>();
        try {
            String query = "Select * From tipos_pase_identificativo";
            ResultSet rs = Util.executeQuery(query);
            tipoPaseList = recuperarListaObjetos(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoPaseList;
    }

    @Override
    public TipoPase getTipoPaseById(int id) {
        TipoPase tipoPase = new TipoPase();
        try {
            String query = "Select * from tipos_pase_identificativo WHERE id=" + id;
            ResultSet rs = Util.executeQuery(query);
            tipoPase = recuperarObjeto(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoPase;
    }

    @Override
    public void deleteTipoPaseById(int id) throws SQLException {
        String function = "{call delete_tipo_pase(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, id);
        statement.execute();
        statement.close();
    }


    @Override
    public int getPassCodeByPassType(String passType){
        int code = -1;
        try{
            String query = "Select * from tipos_pase_identificativo where tipo_pase = '" + passType + "'";
            ResultSet rs = Util.executeQuery(query);
            while(rs.next()){
                code = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return code;
    }

 
    //recuperar objetos
    private List<TipoPase> recuperarListaObjetos(ResultSet set) throws SQLException {
        List<TipoPase> tipoPaseList = new LinkedList<>();
        while (set.next()) {
            tipoPaseList.add(
                    new TipoPase(
                            set.getInt("id"),
                            set.getString("tipo_pase")
                    )
            );
        }
        return tipoPaseList;
    }

    private TipoPase recuperarObjeto(ResultSet set) throws SQLException {

        set.next();
        return new TipoPase(set.getInt("id"), set.getString("tipo_pase"));

    }

}
