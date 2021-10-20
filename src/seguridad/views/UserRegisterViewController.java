package seguridad.views;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import seguridad.models.Rol;
import seguridad.models.User;
import services.ServiceLocator;
import util.Util;

import java.util.List;
import java.util.stream.Collectors;

public class UserRegisterViewController {
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private ComboBox<String> cboxRoles;
    @FXML
    private JFXButton btnEditar;
    @FXML
    private JFXButton btnEliminar;
    @FXML
    private JFXButton btnRegistrar;
    @FXML
    private TableView<User> tableUsers;
    @FXML
    private TableColumn<User,String> nameColumn;
    @FXML
    private TableColumn<User,String> userColumn;
    @FXML
    private TableColumn<User,String> rolColumn;

    private boolean edicion;

    private Stage dialogStage;

    private User selected;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize(){
        this.edicion = false;
        this.txtNombre.setOnKeyTyped(event ->
                Util.eventToSetUpperCaseToFirstNameAndLastName(event,this.txtNombre)
        );
        this.cboxRoles.getItems().setAll(getAllRols());
        initializeTable();
    }

    private List<String> getAllRols(){
        return ServiceLocator.getRolService().getAllRols().stream().
                map(Rol::getNombre).collect(Collectors.toList());
    }

    private void initializeTable(){
        this.tableUsers.getItems().clear();
        ObservableList<User> observableList = FXCollections.observableList(
                ServiceLocator.getUserService().getAllUsersButNoLoggedInUser(LoginViewController.getUserLoggedIn().getUsername())
        );

        this.tableUsers.setItems(observableList);
        this.nameColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData.getValue().getNombre()
                        )
        );
        this.tableUsers.setItems(observableList);
        this.userColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData.getValue().getUsername()
                        )
        );
        this.tableUsers.setItems(observableList);
        this.rolColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                ServiceLocator.getRolService().getRolById(cellData.getValue().getId_rol()).getNombre()
                        )
        );
        this.tableUsers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void enableButtons(){
        if(tableUsers.getSelectionModel().getSelectedItem() != null){
            btnEditar.setDisable(false);
            btnEliminar.setDisable(false);
        }
        else{
            btnEditar.setDisable(true);
            btnEliminar.setDisable(true);
        }
    }

    @FXML
    private void handleRegister(){
        if(!emptyFields()){
                if (password.getText().equals(confirmPassword.getText())) {
                    if(!edicion) {
                        User newUser = new User();
                        newUser.setNombre(txtNombre.getText());
                        newUser.setUsername(txtUsername.getText());
                        newUser.setId_rol(ServiceLocator.getRolService().getRolByName(
                                cboxRoles.getSelectionModel().getSelectedItem()
                                ).getId_rol()
                        );
                        newUser.setPassword(password.getText());

                        if (ServiceLocator.getUserService().getUserByUserName(newUser.getUsername()) != null) {
                            Util.dialogResult("Ya existe el usuario " + newUser.getUsername() + ".", Alert.AlertType.INFORMATION);
                            cleanFields();
                        } else {
                            ServiceLocator.getUserService().saveUser(newUser);
                            initializeTable();
                            cleanFields();
                        }
                    }
                    else{
                        User updateUser = new User();
                        updateUser.setId_user(this.selected.getId_user());
                        updateUser.setNombre(txtNombre.getText());
                        updateUser.setUsername(txtUsername.getText());
                        updateUser.setId_rol(ServiceLocator.getRolService().getRolByName(
                                cboxRoles.getSelectionModel().getSelectedItem()
                                ).getId_rol()
                        );
                        updateUser.setPassword(password.getText());
                        if(ServiceLocator.getUserService().
                                getUserByIdAndUserName(updateUser.getId_user(), updateUser.getUsername())!=null){
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                    "¿Desea editar realmente al usuario " + updateUser.getUsername() + "?"
                                    ,ButtonType.OK, ButtonType.CANCEL);
                            alert.showAndWait();
                            if(alert.getResult() == ButtonType.OK) {
                                ServiceLocator.getUserService().updateUser(updateUser);
                                initializeTable();
                                this.btnEliminar.setDisable(true);
                                this.btnEditar.setDisable(true);
                                this.btnRegistrar.setText("Registrar");
                                this.txtUsername.setDisable(false);
                                cleanFields();
                            }
                        }
                    }
                } else {
                    Util.dialogResult("Las contraseñas no coinciden.", Alert.AlertType.WARNING);
                }
            }
    }

    private void cleanFields(){
        txtUsername.setText(null);
        txtNombre.setText(null);
        password.setText(null);
        confirmPassword.setText(null);
        cboxRoles.getSelectionModel().clearSelection();
    }

    private boolean emptyFields(){
        boolean empty = false;
        String message = "";

        if(txtNombre.getText().isEmpty()){
            message += "- Nombre vacío \n";
            empty = true;
        }
        if(txtUsername.getText().isEmpty()){
            message += "- Nombre de usuario vacío \n";
            empty = true;
        }
        if(password.getText().isEmpty()){
            message += "- Contraseña vacía \n";
            empty = true;
        }
        if(confirmPassword.getText().isEmpty()){
            message += "- Confirmación de contraseña vacía \n";
            empty = true;
        }
        if(cboxRoles.getSelectionModel().isEmpty()){
            message += "- Rol no seleccionado \n";
            empty = true;
        }

        if(empty){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos vacíos");
            alert.setContentText(message);
            alert.showAndWait();
        }

        return empty;
    }

    @FXML
    private void handleEdit(){
        edicion = true;
        this.selected = tableUsers.getSelectionModel().getSelectedItem();
        this.btnRegistrar.setText("Confirmar");
        this.txtNombre.setText(selected.getNombre());
        this.txtUsername.setDisable(true);
        this.txtUsername.setText(selected.getUsername());
        String rol = ServiceLocator.getRolService().getRolById(selected.getId_rol()).getNombre();
        this.cboxRoles.getSelectionModel().select(rol);
    }

    @FXML
    private void handleDelete(){
        this.selected = tableUsers.getSelectionModel().getSelectedItem();
        if(ServiceLocator.getUserService().getUserById(selected.getId_user())!=null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Desea eliminar realmente al usuario " + selected.getUsername() + "?"
                    ,ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.OK) {
                ServiceLocator.getUserService().deleteUserById(selected.getId_user());
                initializeTable();
                this.btnEliminar.setDisable(true);
                this.btnEditar.setDisable(true);
            }
        }
    }

    @FXML
    private void handleCancel(){
        this.dialogStage.close();
    }
}
