package hostel;

import hostel.bo.BOFactory;
import hostel.bo.custom.UserBO;
import hostel.dto.UserDTO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppInitializer extends Application {

    private static final UserBO userBO = (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/hostel/view/LoginForm.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.centerOnScreen();
        primaryStage.show();
        addNewUser();
    }

    private void addNewUser() {
        try {
            ArrayList<UserDTO> allUsers = userBO.getAllUsers();
            if (allUsers.isEmpty()) {
                userBO.saveUser(new UserDTO("UID-001", "User", "user123"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
