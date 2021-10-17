package util;


import org.controlsfx.dialog.ExceptionDialog;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Observable;



/**
 * @author Henry A. Serra Morejon
 */
public class Conexion {

    private static Conexion conexion;
    private Connection connection;
    private String inicio = "jdbc:postgresql:";
    private String direccion_server;
    private Integer port;
    private String nameDB;
    private String user;
    private String password;


    public Conexion() throws IOException, ClassNotFoundException, SQLException {
            try {
                String direccionServidor = ConfigProperties.getProperties().getProperty("BD_HOST");
                int puerto=Integer.valueOf(ConfigProperties.getProperties().getProperty("BD_PORT"));
                String nombreBD=ConfigProperties.getProperties().getProperty("BD_NAME");
                String usuario=ConfigProperties.getProperties().getProperty("BD_USERNAME");
                String contrasenna = ConfigProperties.getProperties().getProperty("BD_PASSWORD");
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL(direccionServidor, puerto, nombreBD), usuario, contrasenna);
                this.direccion_server = direccionServidor;
                this.port = puerto;
                this.nameDB = nombreBD;
                this.user = usuario;
                this.password = contrasenna;

            } catch (SQLException e) {
                ExceptionDialog dialog = new ExceptionDialog(e);
                dialog.showAndWait();
            }

    }

    private static final ConexionObservable OBSERVABLE;

    static {
        OBSERVABLE = new ConexionObservable();
    }

    private String URL(String direccionServidor, Integer puerto, String nombreBD) {
        return inicio + "//" + direccionServidor + ":" + puerto.toString() + "/" + nombreBD;
    }

    public static Conexion getInstance() {
        if (conexion == null)
            try {
                conexion = new Conexion();
            } catch (IOException e) {
                e.printStackTrace();
                synchronized (OBSERVABLE) {
                    OBSERVABLE.setChanged();
                    OBSERVABLE.notifyObservers();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                synchronized (OBSERVABLE) {
                    OBSERVABLE.setChanged();
                    OBSERVABLE.notifyObservers();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                synchronized (OBSERVABLE) {
                    OBSERVABLE.setChanged();
                    OBSERVABLE.notifyObservers();
                }
            }
        return conexion;
    }

    public static Connection getConnection() throws SQLException {
        try {
            if (getInstance().connection == null) {
                conexion = new Conexion();
            } else if (getInstance().connection.isClosed())
                getInstance().connection = DriverManager.getConnection(
                        getInstance().URL(getInstance().direccion_server, getInstance().port, getInstance().nameDB),
                        getInstance().user, getInstance().password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No hay conexion");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return getInstance().connection;
    }

    public static Observable getObservable() {
        return OBSERVABLE;
    }

    private static class ConexionObservable extends Observable {
        @Override
        public synchronized void setChanged() {
            super.setChanged();
        }
    }

    public class ConexionEvent {
        private boolean conected;

        public ConexionEvent(boolean conected) {
            this.conected = conected;
        }

        public boolean isConected() {
            return conected;
        }

        public void setConected(boolean conected) {
            this.conected = conected;
        }
    }

}
