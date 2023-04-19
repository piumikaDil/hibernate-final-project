package hostel.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hostel.bo.BOFactory;
import hostel.bo.custom.ReservationBO;
import hostel.dto.ReservationDTO;
import hostel.validation.Validations;
import hostel.view.tdm.ReservationTM;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.ENTER;

public class ReservationInfoFormController {
    private final ReservationBO reservationBO = (ReservationBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.RESERVATION);
    public JFXTextField txtSearch;
    public JFXButton btnSearch;
    public JFXButton btnRefresh;
    public JFXTextField txtResID;
    public JFXTextField txtDate;
    public JFXTextField txtStID;
    public JFXTextField txtRoomID;
    public JFXTextField txtStatus;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public TableView<ReservationTM> tblReservations;
    public LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    private StackPane stackPane;
    private AnchorPane rootPane;

    public void setPane(StackPane stackPane, AnchorPane rootPane) {
        this.stackPane = stackPane;
        this.rootPane = rootPane;
    }

    public void initialize() {
        tblReservations.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("res_id"));
        tblReservations.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("date"));
        tblReservations.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("student_id"));
        tblReservations.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("room_type_id"));
        tblReservations.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("status"));

        initUI();
        addPattern();

        tblReservations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            btnSave.setText(newValue != null ? "Update" : "Save");
            btnSave.setDisable(newValue == null);

            if (newValue != null) {
                txtResID.setText(newValue.getRes_id());
                txtDate.setText(String.valueOf(newValue.getDate()));
                txtStID.setText(newValue.getStudent_id());
                txtRoomID.setText(newValue.getRoom_type_id());
                txtStatus.setText(newValue.getStatus());

                txtResID.setDisable(false);
                txtDate.setDisable(false);
                txtStID.setDisable(false);
                txtRoomID.setDisable(false);
                txtStatus.setDisable(false);
            }
        });
        txtStatus.setOnAction(event -> btnSave.fire());
        loadAllReservations();
    }

    private void loadAllReservations() {
        tblReservations.getItems().clear();
        /*Get all Reservations*/
        try {
            ArrayList<ReservationDTO> allReservations = reservationBO.getAllReservations();
            for (ReservationDTO dto : allReservations) {
                tblReservations.getItems().add(new ReservationTM(dto.getRes_id(), dto.getDate(), dto.getStudent_id(), dto.getRoom_type_id(), dto.getStatus()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void initUI() {
        txtResID.clear();
        txtDate.clear();
        txtStID.clear();
        txtRoomID.clear();
        txtStatus.clear();
        txtSearch.clear();
        txtResID.setDisable(true);
        txtDate.setDisable(true);
        txtStID.setDisable(true);
        txtRoomID.setDisable(true);
        txtStatus.setDisable(true);
        txtResID.setEditable(false);
        txtDate.setEditable(false);
        txtStID.setEditable(false);
        txtRoomID.setEditable(false);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    private void addPattern() {
        Pattern patternStatus = Pattern.compile("^[A-z ]{4,30}$");// Not edited
        map.put(txtStatus, patternStatus);
    }

    public void textFieldsKeyReleased(KeyEvent keyEvent) {
        Validations.validate(map, btnSave);

        if (keyEvent.getCode() == ENTER) {
            Object response = Validations.validate(map, btnSave);

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            } else if (response instanceof Boolean) {
                btnSaveOnAction();
            }
        }
    }

    public void btnSaveOnAction() {
        if (!txtStatus.getText().matches("^[A-z/ ]{3,30}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Status").show();
            txtStatus.requestFocus();
            txtStatus.selectAll();
            return;
        }
        String resID = txtResID.getText();
        String date = txtDate.getText();
        String stID = txtStID.getText();
        String roomID = txtRoomID.getText();
        String status = txtStatus.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        boolean b = false;
        if (!status.matches("^[A-z ]{4,30}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid status").show();
            txtStatus.requestFocus();
            return;
        }

        /*Update reservation*/
        try {
            b = reservationBO.updateReservation(new ReservationDTO(resID, localDate, stID, roomID, status));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update the reservation " + resID + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (b) {
            ReservationTM selectedRoom = tblReservations.getSelectionModel().getSelectedItem();
            selectedRoom.setStatus(status);
            tblReservations.refresh();
        }
    }

    public void btnDeleteOnAction() {
        /*Delete Reservation*/
        String id = tblReservations.getSelectionModel().getSelectedItem().getRes_id();
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this reservation?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)) {
                reservationBO.deleteReservation(id);
                tblReservations.getItems().remove(tblReservations.getSelectionModel().getSelectedItem());
                tblReservations.getSelectionModel().clearSelection();
                initUI();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the reservation " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void findRoom(String search) {
        tblReservations.getItems().clear();
        try {
            ArrayList<Object[]> reservations = reservationBO.searchReservation(search);
            for (Object[] dto : reservations) {
                if (dto != null) {
                    tblReservations.getItems().add(new ReservationTM((String) dto[0], (LocalDate) dto[1], (String) dto[2], (String) dto[3], (String) dto[4]));
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "No search results.").show();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find the reservation " + search, e);
        }
    }

    public void menuDeleteOnAction(ActionEvent event) {
        btnDeleteOnAction();
    }

    public void txtSearchKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            findRoom(txtSearch.getText());
    }

    public void btnSearchOnAction(ActionEvent event) {
        findRoom(txtSearch.getText());
    }

    public void btnRefreshOnAction(ActionEvent event) {
        initUI();
        loadAllReservations();
    }
}
