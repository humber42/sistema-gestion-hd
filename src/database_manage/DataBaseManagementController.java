package database_manage;

import com.jfoenix.controls.JFXButton;
import icons.ImageLocation;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.ConfigProperties;
import util.Util;
import views.dialogs.DialogLoadingController2;
import views.dialogs.DialogLoadingUrl;

import java.io.*;
import java.nio.file.Paths;

public class DataBaseManagementController {

    private Stage dialogStage;
    @FXML
    private ImageView imagenBoton;

    @FXML
    private TextField databaseName;
    @FXML
    private TextField usuarioName;
    @FXML
    private PasswordField passwordName;
    @FXML
    private TextField servidorName;
    @FXML
    private TextField puertoName;

    @FXML
    private JFXButton guardarButton;
    private static double xOffset = 0;

    private Stage dialogLoading;
    private static double yOffset = 0;
    @FXML
    private Pane topPane;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @FXML
    private void initialize() {
        this.databaseName.setText(ConfigProperties.getProperties().getProperty("BD_NAME"));
        this.passwordName.setText(ConfigProperties.getProperties().getProperty("BD_PASSWORD"));
        this.puertoName.setText(ConfigProperties.getProperties().getProperty("BD_PORT"));
        this.servidorName.setText(ConfigProperties.getProperties().getProperty("BD_HOST"));
        this.usuarioName.setText(ConfigProperties.getProperties().getProperty("BD_USERNAME"));
        this.databaseName.setDisable(true);
        this.passwordName.setDisable(true);
        this.puertoName.setDisable(true);
        this.servidorName.setDisable(true);
        this.usuarioName.setDisable(true);
        this.topPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        this.topPane.setOnMouseDragged(event -> {
            dialogStage.setX(event.getScreenX() - xOffset);
            dialogStage.setY(event.getScreenY() - yOffset);
        });

        this.imagenBoton.setImage(new Image(ImageLocation.class.getResourceAsStream("SQL_48px.png")));
    }

    private boolean notFieldsEmpty() {
        boolean notFieldsEmpty = true;

        if (this.usuarioName.getText().isEmpty()) {
            notFieldsEmpty = false;
        } else if (this.passwordName.getText().isEmpty()) {
            notFieldsEmpty = false;
        } else if (this.servidorName.getText().isEmpty()) {
            notFieldsEmpty = false;
        } else if (this.puertoName.getText().isEmpty()) {
            notFieldsEmpty = false;
        } else if (this.databaseName.getText().isEmpty()) {
            notFieldsEmpty = false;
        }

        return notFieldsEmpty;
    }

    @FXML
    private void guardarNuevosDatos() {
        if (notFieldsEmpty()) {
            ConfigProperties.guardarProperties("BD_USERNAME", this.usuarioName.getText());
            ConfigProperties.guardarProperties("BD_HOST", this.servidorName.getText());
            ConfigProperties.guardarProperties("BD_PORT", this.puertoName.getText());
            ConfigProperties.guardarProperties("BD_NAME", this.databaseName.getText());
            ConfigProperties.guardarProperties("BD_PASSWORD", this.passwordName.getText());
            this.databaseName.setDisable(true);
            this.passwordName.setDisable(true);
            this.puertoName.setDisable(true);
            this.servidorName.setDisable(true);
            this.usuarioName.setDisable(true);

            this.databaseName.setEditable(false);
            this.passwordName.setEditable(false);
            this.puertoName.setEditable(false);
            this.servidorName.setEditable(false);
            this.usuarioName.setEditable(false);
            this.guardarButton.setDisable(true);
        } else {
            Util.dialogResult("Existen campos en blanco", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void makeEditableTextFields() {
        this.databaseName.setDisable(false);
        this.passwordName.setDisable(false);
        this.puertoName.setDisable(false);
        this.servidorName.setDisable(false);
        this.usuarioName.setDisable(false);


        this.databaseName.setEditable(true);
        this.passwordName.setEditable(true);
        this.puertoName.setEditable(true);
        this.servidorName.setEditable(true);
        this.usuarioName.setEditable(true);
        this.guardarButton.setDisable(false);

    }

    @FXML
    private void closeDialog() {
        this.dialogStage.close();
    }

    @FXML
    private void crearBackupDatabase() {
        this.createBackupDatabase("custom");
    }


    private void createBackupDatabase(String format) {
        String host = ConfigProperties.getProperties().getProperty("BD_HOST");
        String port = ConfigProperties.getProperties().getProperty("BD_PORT");
        String user = ConfigProperties.getProperties().getProperty("BD_USERNAME");
        String password = ConfigProperties.getProperties().getProperty("BD_PASSWORD");
        String database = ConfigProperties.getProperties().getProperty("BD_NAME");

        Process proceso;
        ProcessBuilder constructor;


        String path = Util.selectPathToSaveDatabase(this.dialogStage);

        this.crearBackup(database, user, path, System.getProperty("user.dir") + "/src" + "/addons", format);

//        if (path != null) {
//            boolean hecho = false;
//            try {
//                String pathMoment = System.getProperty("user.dir") + "/src" + "/addons" + "/pg_dump.exe";
//                File pgDump = new File(pathMoment);
//                String pgDumpPath = Paths.get(pathMoment).toAbsolutePath().normalize().toString();
//
//                System.out.println(pgDumpPath);
//                if (pgDump.exists()) {
//                    if (!path.equalsIgnoreCase("")) {
//                        String a =format.equalsIgnoreCase("custom")?".backup":".sql";
//                        constructor = new ProcessBuilder(pgDumpPath, "--verbose", "--format", format, "-f", path + "\\" + database + a);
//                    } else {
//                        constructor = new ProcessBuilder(pgDumpPath, "--verbose", "--inserts", "--column-inserts", "-f", path + "\\sys.backup");
//                        System.out.println("ERROR");
//                    }
//                    constructor.environment().put("PGHOST", host);
//                    constructor.environment().put("PGPORT", port);
//                    constructor.environment().put("PGUSER", user);
//                    constructor.environment().put("PGPASSWORD", password);
//                    constructor.environment().put("PGDATABASE", database);
//                    constructor.redirectErrorStream(true);
//                    proceso = constructor.start();
//                    this.escribirProceso(proceso, false);
//                } else {
//                    Util.dialogResult("No se encuentra el pg_dump.exe", Alert.AlertType.INFORMATION);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Util.dialogResult("Error al crear la salva", Alert.AlertType.ERROR);
//            }
//        }
    }

    private boolean crearBackup(String dbName, String username, String backupPath, String bdPath, String format) {
        boolean flag = true;
        Runtime rt = Runtime.getRuntime();
        String pathBat = System.getProperty("user.dir") + "/src" + "/addons/" + "crear_salva.bat";
        File pgSalva = new File(pathBat);
        Process process;
        String pgSalvaPath = Paths.get(pgSalva.getAbsolutePath()).toAbsolutePath().normalize().toString();
        if (pgSalva.exists()) {
            StringBuffer cmdBuffer = new StringBuffer();
            cmdBuffer.append(pgSalvaPath);
            cmdBuffer.append(" save ");
            cmdBuffer.append("-r pg_dump.exe ");
            cmdBuffer.append("-m");
            cmdBuffer.append(" --verbose");
            cmdBuffer.append(" -F ");
            cmdBuffer.append(format);
            cmdBuffer.append(" -h ");
            cmdBuffer.append(ConfigProperties.getProperties().getProperty("BD_HOST"));
            cmdBuffer.append(" -p ");
            cmdBuffer.append(ConfigProperties.getProperties().getProperty("BD_PORT"));
            cmdBuffer.append(" -u ");
            cmdBuffer.append(username);
            cmdBuffer.append(" -enc utf-8");
            cmdBuffer.append(" -archivo ");
            cmdBuffer.append(backupPath);
            cmdBuffer.append(dbName);
            if (format.equalsIgnoreCase("custom"))
                cmdBuffer.append(".backup ");
            else if (format.equalsIgnoreCase("plain"))
                cmdBuffer.append(".sql ");
            else if (format.equalsIgnoreCase("tar"))
                cmdBuffer.append(".tar ");
            cmdBuffer.append("-db ");
            cmdBuffer.append(dbName);
            //crear_salva.bat salva -r pg_dump.exe -m --verbose -F p -h localhost -p 5432 -u postgres -enc "utf-8" -archivo "D:\\sysSP.sql" -db sysSP

            try {
                System.out.println(cmdBuffer);
                process = rt.exec(cmdBuffer.toString());

            } catch (IOException e) {
                flag = false;
                e.printStackTrace();
            }
        } else {
            Util.dialogResult("No existe el archivo necesario", Alert.AlertType.ERROR);
        }
        return flag;
    }

    private boolean restaurarBasedeDatos(String dbName, String username, String backupPath, String bdPath, String format) {
        boolean flag = true;
        Runtime rt = Runtime.getRuntime();
        String pathBat = System.getProperty("user.dir") + "/src" + "/addons/" + "crear_salva.bat";
        File pgSalva = new File(pathBat);
        Process process;
        String pgSalvaPath = Paths.get(pgSalva.getAbsolutePath()).toAbsolutePath().normalize().toString();
        if (pgSalva.exists()) {
            StringBuffer cmdBuffer = new StringBuffer();
            cmdBuffer.append(pgSalvaPath);
            cmdBuffer.append(" restore ");
            cmdBuffer.append("-r pg_restore.exe ");
            cmdBuffer.append("-m");
            cmdBuffer.append(" --verbose");
            cmdBuffer.append(" -h ");
            cmdBuffer.append(ConfigProperties.getProperties().getProperty("BD_HOST"));
            cmdBuffer.append(" -p ");
            cmdBuffer.append(ConfigProperties.getProperties().getProperty("BD_PORT"));
            cmdBuffer.append(" -u ");
            cmdBuffer.append(username);
            cmdBuffer.append(" -enc utf-8");
            cmdBuffer.append(" -archivo ");
            cmdBuffer.append(backupPath);
            cmdBuffer.append(" -pass ");
            cmdBuffer.append(ConfigProperties.getProperties().getProperty("BD_PASSWORD"));
            cmdBuffer.append(" -db ");
            cmdBuffer.append(dbName);

            try {
                System.out.println(cmdBuffer);
                process = rt.exec(cmdBuffer.toString());

            } catch (IOException e) {
                flag = false;
                e.printStackTrace();
            }
        } else {
            Util.dialogResult("No existe el archivo necesario", Alert.AlertType.ERROR);
        }
        return flag;
    }

    @FXML
    private void restore_SALVA() {

        ProcessBuilder constructor;
        Process proceso;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PostgreSQL Backups", "*.backup")
        );
        File fileImage = fileChooser.showOpenDialog(this.dialogStage);
        String path = fileImage.getAbsolutePath();
        if (fileImage != null || !path.isEmpty()) {
            String host = ConfigProperties.getProperties().getProperty("BD_HOST");
            String port = ConfigProperties.getProperties().getProperty("BD_PORT");
            String user = ConfigProperties.getProperties().getProperty("BD_USERNAME");
            String password = ConfigProperties.getProperties().getProperty("BD_PASSWORD");
            String database = ConfigProperties.getProperties().getProperty("BD_NAME");
            //String database = "test";

            try {
//                String pathMoment = System.getProperty("user.dir") + "/src" + "/addons" + "/pg_restore.exe";
//                File pgRestore = new File(pathMoment);
//                String pgRestorePath = Paths.get(pathMoment).toAbsolutePath().normalize().toString();
//                System.out.println(pgRestorePath);
//                if (pgRestore.exists()) {
//                    constructor = new ProcessBuilder(pgRestorePath, "--host", host
//                            , "--port", port, "--username", user, "--dbname", database, "--verbose", path);
//                    constructor.environment().put("PGPASSWORD", password);
//                    constructor.redirectErrorStream(true);
//
//                    proceso = constructor.start();
//                    this.escribirProceso(proceso, true);
//
//                } else {
//                    Util.dialogResult("No se encuentra el archivo pg_restore.exe", Alert.AlertType.INFORMATION);
//                }
                this.restaurarBasedeDatos(database, user, path, "", "");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void escribirProceso(Process process, boolean restore) {
        Task<Boolean> task = new Task<Boolean>() {
            int exitValue;

            @Override
            protected Boolean call() throws Exception {
                try {
                    OutputStream stdIn = process.getOutputStream();
                    InputStream stdOut = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stdOut));
                    BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(stdIn));
                    String input = null;
                    int i = 0;
                    while (!(input = reader.readLine()).isEmpty()) {
                        System.out.println(input);
                        writter.write(input);
                        i++;
                        System.out.println(i);
                        writter.newLine();

                    }
                    writter.flush();
                    reader.close();
                    writter.close();
//                    exitValue = process.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                dialogLoading.close();
                Util.dialogResult("Terminado", Alert.AlertType.INFORMATION);
                //System.out.println(exitValue);
            }

            @Override
            protected void failed() {
                super.failed();
                dialogLoading.close();
                Util.dialogResult("Terminado", Alert.AlertType.ERROR);
            }
        };
        Thread th = new Thread(task);
        loadDialogLoading(this.dialogStage, process, restore);
        th.start();

    }

    private void loadDialogLoading(Stage mainApp, Process process, boolean restore) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DialogLoadingUrl.class.getResource("DialogLoading2.fxml"));
            StackPane panel = loader.load();
            dialogLoading = new Stage();
            dialogLoading.setScene(new Scene(panel));
            dialogLoading.initModality(Modality.WINDOW_MODAL);
            dialogLoading.initOwner(mainApp);
            dialogLoading.initStyle(StageStyle.UNDECORATED);
            DialogLoadingController2 controller = loader.getController();
            if (restore) {
                controller.setLabelText("Restaurando");
            } else {
                controller.setLabelText("Salvando");
            }
            //controller.setLabelText("Cargando");
            controller.setProcess(process);
            dialogLoading.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void cargarSqlIntoDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Sql Files", "*.sql"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File sqlFile = fileChooser.showOpenDialog(this.dialogStage);

        try {
            String host = ConfigProperties.getProperties().getProperty("BD_HOST");
            String port = ConfigProperties.getProperties().getProperty("BD_PORT");
            String user = ConfigProperties.getProperties().getProperty("BD_USERNAME");
            String password = ConfigProperties.getProperties().getProperty("BD_PASSWORD");
            String database = ConfigProperties.getProperties().getProperty("BD_NAME");

            if (sqlFile != null) {
                try {
                    String pathMoment = System.getProperty("user.dir") + "/src" + "/addons" + "/psql.exe";
                    Runtime rt = Runtime.getRuntime();
                    String executeSqlComand = pathMoment + " -U " + user + " -h " + host + " -p " + port + " -d " +
                            database + " -f " + sqlFile.getAbsolutePath();
                    Process process = rt.exec(executeSqlComand);
                    this.escribirProceso(process, false);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        } catch (Exception e) {
            Util.dialogResult("No se pudo ejecutar el fichero", Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void generateSqlFile() {
        this.createBackupDatabase("plain");
    }

}
