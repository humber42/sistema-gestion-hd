package sistema_identificativo.views.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import icons.ImageLocation;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.util.StringUtils;
import services.ServiceLocator;
import sistema_identificativo.models.RegistroPase;
import util.ConfigProperties;
import util.PostFile;
import util.Util;
import views.dialogs.DialogLoadingController;
import views.dialogs.DialogLoadingUrl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AddPicturePendingToPass {

    private Stage dialogStage;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelNumeroPase;
    @FXML
    private Label labelCarneIdentidad;
    @FXML
    private Label labelTipoPase;
    @FXML
    private Label labelCategoriaPase;
    @FXML
    private ListView<String> listaPases;
    @FXML
    private ImageView profilePhoto;
    @FXML
    private JFXRadioButton changePhoto;
    @FXML
    private JFXRadioButton addPhotoRadioBtn;
    @FXML
    private TitledPane panePases;

    @FXML
    private JFXButton annadir;
    @FXML
    private JFXButton guardar;
    @FXML
    private JFXTextField textName;
    @FXML
    private AnchorPane panelImagen;

    private RegistroPase paseSeleccionado;
    private Stage dialogUploading;

    private boolean changePicture;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        this.changePhoto.setSelected(false);
        this.addPhotoRadioBtn.setSelected(true);
        this.addPhotoRadioBtn.setOnMouseClicked(event -> {
            if (this.addPhotoRadioBtn.isSelected()) {
                this.textName.setText(null);
                this.changePhoto.setSelected(false);
                this.panePases.setText("Pases pendientes de foto");
                this.annadir.setDisable(true);
                this.guardar.setDisable(true);
                this.listaPases.getItems().clear();
                this.listaPases.getItems().addAll(
                        ServiceLocator.getRegistroPaseService().pasesPendientesFoto()
                );
                this.profilePhoto.setImage(new Image(ImageLocation.class.getResource("no-img.jpg").toString()));
                System.out.println("sin foto " + listaPases.getItems().size());
                this.changePicture = false;
            } else {
                this.addPhotoRadioBtn.setSelected(true);
            }
        });
        this.changePhoto.setOnMouseClicked(event -> {
            if (this.changePhoto.isSelected()) {
                this.textName.setText(null);
                this.addPhotoRadioBtn.setSelected(false);
                this.annadir.setDisable(true);
                this.guardar.setDisable(true);
                this.panePases.setText("Pases con foto");
                this.listaPases.getItems().clear();
                this.listaPases.getItems().addAll(
                        ServiceLocator.getRegistroPaseService().pasesConFoto()
                );
                this.profilePhoto.setImage(new Image(ImageLocation.class.getResource("no-img.jpg").toString()));
                System.out.println("con foto " + listaPases.getItems().size());
                this.changePicture = true;
            } else {
                this.changePhoto.setSelected(true);
            }
        });

        this.textName.setOnKeyTyped(event -> {
            Util.eventToSetUpperCaseToFirstNameAndLastName(event, this.textName);
            this.applySearch();
        });

        this.labelCarneIdentidad.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNumeroIdentidad() : "(No hay datos seleccionados)");
        this.labelCategoriaPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getCodigoPase().getCodigo() : "(No hay datos seleccionados)");
        this.labelNombre.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNombre() : "(No hay datos seleccionados)");
        this.labelNumeroPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNumeroPase() : "(No hay datos seleccionados)");
        this.labelTipoPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getTipoPase().getTipoPase() : "(No hay datos seleccionados)");

        this.listaPases.getItems().addAll(
                ServiceLocator.getRegistroPaseService().pasesPendientesFoto()
        );

        this.listaPases.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.listaPases.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    this.setValuesOnToLabels(newValue);
                    if (this.changePicture)
                        this.annadir.setDisable(true);
                    else
                        this.annadir.setDisable(false);
                    if (!this.profilePhoto.getImage().getUrl().contains("no-img.jpg")) {
                        this.guardar.setDisable(false);
                    }
                }
        );

        this.panelImagen.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                mouseDragOver(event);
            }
        });

        this.panelImagen.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                onMouseDragDropped(event);
            }
        });


        this.profilePhoto.imageProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                if (Objects.nonNull(paseSeleccionado))
                    this.guardar.setDisable(false);
            }
        });

    }

    private void addImage(Image i) {
        this.profilePhoto.setImage(i);
    }

    private void onMouseDragDropped(final DragEvent e) {
        final Dragboard dragboard = e.getDragboard();
        boolean success = false;
        if (dragboard.hasFiles()) {
            success = true;
            final File file = dragboard.getFiles().get(0);

            System.out.println(file.getAbsolutePath());

            javafx.scene.image.Image image = new Image("file:" + file.getAbsolutePath());
            addImage(image);
        }
        e.setDropCompleted(success);
        e.consume();
    }

    private void mouseDragOver(final DragEvent e) {
        final Dragboard db = e.getDragboard();
        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".png") ||
                db.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg") ||
                db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg");
        if (db.hasFiles()) {
            if (isAccepted) {
                e.acceptTransferModes(TransferMode.COPY);
            } else {
                e.consume();
            }
        }
    }

    private void applySearch() {
        this.listaPases.getItems().clear();
        if (this.changePicture) {
            this.listaPases.getItems().addAll(
                    this.textName.getText().isEmpty() ?
                            ServiceLocator.getRegistroPaseService().pasesConFoto() :
                            ServiceLocator.getRegistroPaseService().getAllWithPhotosByContainName(this.textName.getText())
            );
        } else {
            this.listaPases.getItems().addAll(
                    this.textName.getText().isEmpty() ?
                            ServiceLocator.getRegistroPaseService().pasesPendientesFoto() :
                            ServiceLocator.getRegistroPaseService().getAllPendingPhotosByContainName(this.textName.getText())
            );
        }
    }

    @FXML
    private void searchPicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File fileImage = fileChooser.showOpenDialog(this.dialogStage);

        if (fileImage != null) {
            javafx.scene.image.Image image = new Image("file:" + fileImage.getAbsolutePath());
            this.profilePhoto.setImage(image);
            this.guardar.setDisable(false);
        }
    }

    @FXML
    private void savePhoto() {
        Task<Boolean> tarea = new Task<Boolean>() {
            private boolean state = true;

            @Override
            protected Boolean call() throws Exception {
                uploadPhoto();
                String image = StringUtils.cleanPath(new File(Util.renombrarPath(profilePhoto.getImage().getUrl())).getName()).replace(" ", "%20");
                try {
                    ServiceLocator.getRegistroPaseService().addPictureToRegistroPase(image, paseSeleccionado.getIdReg());
                } catch (SQLException e) {
                    this.state = false;
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                dialogUploading.close();
                if (state) {
                    Util.dialogResult("La foto fue añadida con éxito.", Alert.AlertType.INFORMATION);
                } else {
                    Util.dialogResult("Ocurrió un error añadiendo la foto", Alert.AlertType.ERROR);
                }
                cleanData();
                annadir.setDisable(true);
                guardar.setDisable(true);
            }
        };
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DialogLoadingUrl.class.getResource("DialogLoading.fxml"));
            StackPane panel = loader.load();
            dialogUploading = new Stage();
            dialogUploading.setScene(new Scene(panel));
            dialogUploading.initOwner(this.dialogStage);
            dialogUploading.initModality(Modality.WINDOW_MODAL);
            dialogUploading.initStyle(StageStyle.UNDECORATED);
            DialogLoadingController controller = loader.getController();
            controller.setLabelText("Subiendo foto");
            dialogUploading.show();
            Thread th = new Thread(tarea);
            th.setDaemon(true);
            th.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void cleanData() {
        this.listaPases.getItems().clear();
        this.listaPases.getItems().addAll(
                ServiceLocator.getRegistroPaseService().pasesPendientesFoto()
        );
        this.paseSeleccionado = null;
        this.labelCarneIdentidad.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNumeroIdentidad() : "(No hay datos seleccionados)");
        this.labelCategoriaPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getCodigoPase().getCodigo() : "(No hay datos seleccionados)");
        this.labelNombre.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNombre() : "(No hay datos seleccionados)");
        this.labelNumeroPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNumeroPase() : "(No hay datos seleccionados)");
        this.labelTipoPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getTipoPase().getTipoPase() : "(No hay datos seleccionados)");
        this.profilePhoto.setImage(new Image(ImageLocation.class.getResource("no-img.jpg").toString()));
        this.textName.setText("");
    }

    private void uploadPhoto() {
        try {
            PostFile postFile = new PostFile(ConfigProperties.getProperties().getProperty("URL_IMAGE_SERVER"),
                    Util.renombrarPath(this.profilePhoto.getImage().getUrl()));
            postFile.post();
        } catch (Exception e) {
            Logger.getLogger(PostFile.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void closeDialog() {
        dialogStage.close();
    }

    private void setValuesOnToLabels(String value) {
        if (value != null) {
            this.paseSeleccionado = ServiceLocator.getRegistroPaseService().getPaseByPassName(value);
            this.labelCarneIdentidad.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNumeroIdentidad() : "(No hay datos)");
            this.labelCategoriaPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getCodigoPase().getCodigo() : "(No hay datos)");
            this.labelNombre.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNombre() : "(No hay datos)");
            this.labelNumeroPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNumeroPase() : "(No hay datos)");
            this.labelTipoPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getTipoPase().getTipoPase() : "(No hay datos)");
            if (this.changePicture) {
                String URL_IMAGE_PASE = ConfigProperties.getProperties().getProperty("URL_DOWNLOAD_IMAGE") + paseSeleccionado.getImageUrl();
                System.out.println(URL_IMAGE_PASE);
                this.profilePhoto.setImage(new Image(URL_IMAGE_PASE));
            } else
                this.profilePhoto.setImage(new Image(ImageLocation.class.getResource("no-img.jpg").toString()));
        } else {
            this.labelCarneIdentidad.setText("(No hay datos)");
            this.labelCategoriaPase.setText("(No hay datos)");
            this.labelNombre.setText("(No hay datos)");
            this.labelNumeroPase.setText("(No hay datos)");
            this.labelTipoPase.setText("(No hay datos)");
        }
    }
}
