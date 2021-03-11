package util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * @author Henry A. Serra Morejon
 */
public class Conexion {

    private static Conexion conexion;
    private Connection connection;
    private File f = new File("src/util/server.txt");
    private String inicio = "jdbc:postgresql:";
    private String direccion_server;
    private Integer port;
    private String nameDB;
    private String user;
    private String password;


    public Conexion() throws IOException, ClassNotFoundException, SQLException {
        if (!f.exists())
            throw new IOException("No existe el archivo server.txt para la conexión");
        else {
            RandomAccessFile r = new RandomAccessFile(f, "r");
            String direccionServidor = r.readUTF();
            Integer puerto = r.readInt();
            String nombreBD = r.readUTF();
            String usuario = r.readUTF();
            String contrasenna = r.readUTF();

            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL(direccionServidor, puerto, nombreBD), usuario, contrasenna);

                this.direccion_server = direccionServidor;
                this.port = puerto;
                this.nameDB = nombreBD;
                this.user = usuario;
                this.password = contrasenna;

            } catch (SQLException e) {
                r.close();
                System.err.println(e);
            }
            r.close();
        }
    }

    /**
     * @param direccionServidor
     * @param puerto
     * @param nombreBD
     * @param usuario
     * @param contrasenna
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Conexion(String direccionServidor, int puerto, String nombreBD, String usuario, String contrasenna)
            throws IOException, ClassNotFoundException, SQLException {
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(URL(direccionServidor, puerto, nombreBD), usuario, contrasenna);

            this.direccion_server = direccionServidor;
            this.port = puerto;
            this.nameDB = nombreBD;
            this.user = usuario;
            this.password = contrasenna;


            RandomAccessFile r = new RandomAccessFile(f, "rw");
            r.writeUTF(this.direccion_server);
            r.writeInt(this.port);
            r.writeUTF(this.nameDB);
            r.writeUTF(usuario);
            r.writeUTF(contrasenna);
            r.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Conexion getInstance() {
        if (conexion == null)
            try {
                conexion = new Conexion();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return conexion;
    }

    public static Connection getConnection() {
        try {
            if (getInstance().connection.isClosed())
                getInstance().connection = DriverManager.getConnection(
                        getInstance().URL(getInstance().direccion_server, getInstance().port, getInstance().nameDB),
                        getInstance().user, getInstance().password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return getInstance().connection;
    }

    private String URL(String direccionServidor, Integer puerto, String nombreBD) {
        return inicio + "//" + direccionServidor + ":" + puerto.toString() + "/" + nombreBD;
    }

}
