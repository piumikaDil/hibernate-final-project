package hostel.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import hostel.bo.BOFactory;
import hostel.bo.custom.ReservationBO;
import hostel.dto.ReservationDTO;
import hostel.dto.RoomDTO;
import hostel.dto.StudentDTO;
import hostel.view.tdm.ReservationTM;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationFormController {
    public Label lblResID;
    public JFXComboBox<String> cmbStID;
    public JFXComboBox<String> cmbRoomTypeID;
    public JFXButton btnAdd;
    public JFXTextField txtStName;
    public JFXTextField txtType;
    public JFXTextField txtKeyMoney;
    public JFXTextField txtStAddress;
    public JFXTextField txtStatus;
    public TableView<ReservationTM> tblReservation;
    public TextField txtKeyMoneyAmount;
    public TextField txtCash;
    public TextField txtChange;
    public JFXButton btnCancel;
    public JFXButton btnConfirmReservation;
    ReservationBO reservationBO = (ReservationBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.RESERVATION);
    private StackPane stackPane;
    private AnchorPane rootPane;
    private String resId;

    public void setPane(StackPane stackPane, AnchorPane rootPane) {
        this.stackPane = stackPane;
        this.rootPane = rootPane;
    }

    public void initialize() {
        tblReservation.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("student_id"));
        tblReservation.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblReservation.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("room_type_id"));
        tblReservation.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("type"));
        tblReservation.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("key_money"));
        tblReservation.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<ReservationTM, Button> lastCol = (TableColumn<ReservationTM, Button>) tblReservation.getColumns().get(6);
        lastCol.setCellValueFactory(param -> {
            Button btnDelete = new Button("Delete");
            btnDelete.setStyle("-fx-text-fill: #000000;");
            btnDelete.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this record?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();

                if (buttonType.get().equals(ButtonType.YES)) {
                    tblReservation.getItems().remove(param.getValue());
                    tblReservation.getSelectionModel().clearSelection();
                    txtKeyMoneyAmount.setText("");
                    enableOrDisableReservationButton();
                }
            });
            return new ReadOnlyObjectWrapper<>(btnDelete);
        });
        resId = generateNewResId();
        lblResID.setText("Res. ID: " + resId);
        btnConfirmReservation.setDisable(true);
        txtStName.setFocusTraversable(false);
        txtStName.setEditable(false);
        txtStAddress.setFocusTraversable(false);
        txtStAddress.setEditable(false);
        txtType.setFocusTraversable(false);
        txtType.setEditable(false);
        txtKeyMoney.setFocusTraversable(false);
        txtKeyMoney.setEditable(false);
        txtStatus.setOnAction(event -> btnAdd.fire());
        txtStatus.setEditable(false);
        btnAdd.setDisable(true);

        cmbStID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            enableOrDisableReservationButton();

            if (newValue != null) {
                try {
                    /*Search Student*/
                    try {
                        if (!existStudent(newValue + "")) {
                            new Alert(Alert.AlertType.ERROR, "There is no such student associated with the id " + newValue + "").show();
                        }

                        StudentDTO search = reservationBO.searchStudent(newValue + "");
                        txtStName.setText(search.getName());
                        txtStAddress.setText(search.getAddress());

                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to find the student " + newValue + "" + e).show();
                    }
                } catch (ClassNotFoundException throwable) {
                    throwable.printStackTrace();
                }
            } else {
                txtStName.clear();
                txtStAddress.clear();
            }
        });

        cmbRoomTypeID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            txtStatus.setEditable(newValue != null);
            btnAdd.setDisable(newValue == null);

            if (newValue != null) {

                /*Find Room*/
                try {
                    if (!existRoom(newValue + "")) {
                        new Alert(Alert.AlertType.ERROR, "There is no such room associated with the id " + newValue + "").show();
                    }

                    //Search room
                    RoomDTO room = reservationBO.searchRoom(newValue + "");
                    txtType.setText(room.getType());
                    txtKeyMoney.setText(room.getKey_money());

                } catch (SQLException | ClassNotFoundException throwable) {
                    throwable.printStackTrace();
                }
            } else {
                txtType.clear();
                txtKeyMoney.clear();
                txtStatus.clear();
            }
        });

        tblReservation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedReservation) -> {

            if (selectedReservation != null) {
                cmbRoomTypeID.setDisable(true);
                cmbRoomTypeID.setValue(selectedReservation.getRoom_type_id());
                btnAdd.setText("Update");
                txtStatus.setText(selectedReservation.getStatus() + "");
            } else {
                btnAdd.setText("Add");
                cmbRoomTypeID.setDisable(false);
                cmbRoomTypeID.getSelectionModel().clearSelection();
                txtStatus.clear();
            }
        });
        loadAllStudentIds();
        loadAllRoomTypeIds();
    }

    private boolean existRoom(String id) throws SQLException, ClassNotFoundException {
        return reservationBO.checkRoomIsAvailable(id);
    }

    boolean existStudent(String id) throws SQLException, ClassNotFoundException {
        return reservationBO.checkStudentIsAvailable(id);
    }

    private String generateNewResId() {
        String id = "";
        try {
            id = reservationBO.generateNewResID();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new id " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        int newRoomId = Integer.parseInt(id.replace("RID-", "")) + 1;
        return String.format("RID-%03d", newRoomId);
    }

    private void loadAllStudentIds() {
        try {
            ArrayList<StudentDTO> all = reservationBO.getAllStudents();
            for (StudentDTO studentDTO : all) {
                cmbStID.getItems().add(studentDTO.getStudent_id());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load student ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllRoomTypeIds() {
        try {
            /*Get all rooms*/
            ArrayList<RoomDTO> all = reservationBO.getAllRooms();
            for (RoomDTO dto : all) {
                cmbRoomTypeID.getItems().add(dto.getRoom_type_id());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        if (!txtStatus.getText().matches("^[A-z/ ]{3,30}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Status").show();
            txtStatus.requestFocus();
            txtStatus.selectAll();
            return;
        }
        String studentId = cmbStID.getSelectionModel().getSelectedItem();
        String name = txtStName.getText();
        String roomTypeId = cmbRoomTypeID.getSelectionModel().getSelectedItem();
        String type = txtType.getText();
        String keyMoney = txtKeyMoney.getText();
        String status = txtStatus.getText();

        tblReservation.getItems().add(new ReservationTM(studentId, name, roomTypeId, type, keyMoney, status));
        cmbStID.getSelectionModel().clearSelection();
        cmbRoomTypeID.getSelectionModel().clearSelection();
        txtKeyMoneyAmount.setText(keyMoney);
        enableOrDisableReservationButton();
    }

    private void enableOrDisableReservationButton() {
        btnConfirmReservation.setDisable(tblReservation.getItems().isEmpty());
    }

    public void btnConfirmReservationOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to confirm this reservation?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)) {
            boolean b = saveReservation(tblReservation.getItems().stream().map(tm -> new ReservationDTO(resId, LocalDate.now(), tm.getStudent_id(), tm.getRoom_type_id(), tm.getStatus())).collect(Collectors.toList()));
            if (b) {
                new Alert(Alert.AlertType.INFORMATION, "Room has been reserved successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Room has not been reserved successfully.").show();
            }
        }
        resId = generateNewResId();
        lblResID.setText("Res. ID: " + resId);
        cmbStID.getSelectionModel().clearSelection();
        cmbRoomTypeID.getSelectionModel().clearSelection();
        tblReservation.getItems().clear();
        txtStatus.clear();
        txtKeyMoneyAmount.setText("");
        txtCash.clear();
        txtChange.clear();
    }

    public boolean saveReservation(List<ReservationDTO> reservations) {
        try {
            return reservationBO.saveReservation(new ReservationDTO(reservations));
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }
        return false;
    }

    public void btnCancelOnAction(ActionEvent event) {
        cmbStID.getSelectionModel().clearSelection();
        cmbRoomTypeID.getSelectionModel().clearSelection();
        txtStatus.clear();
        txtKeyMoneyAmount.setText("");
        txtCash.clear();
        txtChange.clear();
        tblReservation.getItems().clear();
    }

    public void txtChangeKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            calculateChange();
    }

    private void calculateChange() {
        BigDecimal change = new BigDecimal(txtCash.getText()).subtract(new BigDecimal(txtKeyMoneyAmount.getText())).setScale(2);
        txtChange.setText(String.valueOf(change));
    }
}
