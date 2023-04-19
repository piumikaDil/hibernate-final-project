package hostel.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.transitions.JFXFillTransition;
import hostel.AppInitializer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuFormController {
    public StackPane stackRootPane;
    public AnchorPane anchorPane;
    public AnchorPane centerPane;
    public Label lblUser;
    public Label lblDate;
    public Label lblTime;
    public ImageView imgUser;
    public ImageView imgLogo;
    public JFXButton btnDashboard;
    public JFXButton btnManageStudents;
    public JFXButton btnReservation;
    public JFXButton btnManageRooms;
    public JFXButton btnReservationInfo;
    private JFXButton activeMenuButton;
    private JFXFillTransition ft;

    public void initialize() {
        imgUser.setImage(new Image(AppInitializer.class.getResource("/hostel/view/asset/img/account_circle_black.jpg").toString()));
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss a");
            lblDate.setText(LocalDateTime.now().format(dateFormat));
            lblTime.setText(LocalDateTime.now().format(timeFormat));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        ft = new JFXFillTransition();
        activeMenuButton = btnDashboard;
        btnDashboard.fire();
    }

    public void menuButtonMouseEntered(MouseEvent event) {
        if (event.getSource() != activeMenuButton) {
            ft = new JFXFillTransition();
            ft.setRegion((JFXButton) event.getSource());
            ft.setDuration(new Duration(500));
            ft.setFromValue(Color.WHITE);
            ft.setToValue(Color.rgb(151, 136, 134));
            ft.play();
        }
    }

    public void menuButtonMouseExited(MouseEvent event) {
        if (event.getSource() != activeMenuButton) {
            ft = new JFXFillTransition();
            ft.setRegion((JFXButton) event.getSource());
            ft.setDuration(new Duration(500));
            ft.setFromValue(Color.rgb(151, 136, 134));
            ft.setToValue(Color.WHITE);
            ft.play();
        }
    }

    public void selectMenu(String fxmlName, ActionEvent event, String controllerClass) {
        if (ft.getStatus() == Animation.Status.RUNNING)
            ft.stop();
        activeMenuButton.getStyleClass().remove("menuButtonActive");
        ((JFXButton) event.getSource()).getStyleClass().add("menuButtonActive");
        activeMenuButton = ((JFXButton) event.getSource());
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(AppInitializer.class.getResource(fxmlName).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (controllerClass.equalsIgnoreCase("Dashboard"))
            ((DashboardFormController) fxmlLoader.getController()).setPane(stackRootPane, anchorPane);
        else if (controllerClass.equalsIgnoreCase("Student"))
            ((StudentFormController) fxmlLoader.getController()).setPane(stackRootPane, anchorPane);
        else if (controllerClass.equalsIgnoreCase("Reservation"))
            ((ReservationFormController) fxmlLoader.getController()).setPane(stackRootPane, anchorPane);
        else if (controllerClass.equalsIgnoreCase("Room"))
            ((RoomFormController) fxmlLoader.getController()).setPane(stackRootPane, anchorPane);
        else if (controllerClass.equalsIgnoreCase("ReservationInfo"))
            ((ReservationInfoFormController) fxmlLoader.getController()).setPane(stackRootPane, anchorPane);
        AnchorPane root = fxmlLoader.getRoot();
        centerPane.getChildren().clear();
        centerPane.getChildren().add(root);

        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
    }

    public void btnDashboardClick(ActionEvent event) {
        selectMenu("../hostel/view/DashboardForm.fxml", event, "Dashboard");
    }

    public void btnManageStudentsClick(ActionEvent event) {
        selectMenu("../hostel/view/StudentForm.fxml", event, "Student");
    }

    public void btnReservationClick(ActionEvent event) {
        selectMenu("../hostel/view/ReservationForm.fxml", event, "Reservation");
    }

    public void btnManageRoomsClick(ActionEvent event) {
        selectMenu("../hostel/view/RoomForm.fxml", event, "Room");
    }

    public void btnReservationInfoClick(ActionEvent event) {
        selectMenu("../hostel/view/ReservationInfoForm.fxml", event, "ReservationInfo");
    }
}
