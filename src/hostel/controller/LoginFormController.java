package hostel.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import hostel.AppInitializer;
import hostel.bo.BOFactory;
import hostel.bo.custom.UserBO;
import hostel.dto.UserDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginFormController {
    public JFXTextField txtUsername;
    public JFXPasswordField txtPassword;
    public StackPane stackPane;
    public AnchorPane rootPane;
    public ImageView bgImage;
    public JFXTextField txtShowPwd;
    public JFXCheckBox chBxPassword;
    UserBO userBO = (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    public void initialize() {
        txtShowPwd.setManaged(false);
        txtShowPwd.setVisible(false);

        txtShowPwd.managedProperty().bind(chBxPassword.selectedProperty());
        txtShowPwd.visibleProperty().bind(chBxPassword.selectedProperty());

        txtPassword.managedProperty().bind(chBxPassword.selectedProperty().not());
        txtPassword.visibleProperty().bind(chBxPassword.selectedProperty().not());

        txtShowPwd.textProperty().bindBidirectional(txtPassword.textProperty());
    }

    public void btnCancelClick(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void btnLoginClick(ActionEvent event) {
        if (isAuthorized()) {
            openDashboard();
        } else {
            new Alert(Alert.AlertType.WARNING, "You have entered an invalid username or password. Please try again.").show();
        }
    }

    private boolean isAuthorized() {
        try {
            ArrayList<UserDTO> allUsers = userBO.getAllUsers();
            for (UserDTO dto : allUsers) {
                if (txtUsername.getText().equals(dto.getUsername()) && txtPassword.getText().equals(dto.getPassword())) {
                    return true;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AppInitializer.class.getResource("../hostel/view/MenuForm.fxml"));
            loader.load();
            Parent parent = loader.getRoot();
            Scene scene = new Scene(parent);
            Stage dashboardStage = new Stage();
            dashboardStage.setMinHeight(626.0);
            dashboardStage.setMinWidth(926.0);
            dashboardStage.setScene(scene);
            dashboardStage.setMaximized(true);
            dashboardStage.show();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

