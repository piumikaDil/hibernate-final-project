package hostel.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hostel.bo.BOFactory;
import hostel.bo.custom.RoomBO;
import hostel.dto.RoomDTO;
import hostel.validation.Validations;
import hostel.view.tdm.RoomTM;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.ENTER;

public class RoomFormController {
    private final RoomBO roomBo = (RoomBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ROOM);
    public JFXTextField txtSearch;
    public JFXButton btnSearch;
    public JFXButton btnRefresh;
    public JFXTextField txtRoomID;
    public JFXTextField txtType;
    public JFXTextField txtKeyMoney;
    public JFXTextField txtRoomsQty;
    public JFXButton btnAddNew;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public TableView<RoomTM> tblRooms;
    public LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    private StackPane stackPane;
    private AnchorPane rootPane;

    public void setPane(StackPane stackPane, AnchorPane rootPane) {
        this.stackPane = stackPane;
        this.rootPane = rootPane;
    }

    public void initialize() {
        tblRooms.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("room_type_id"));
        tblRooms.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("type"));
        tblRooms.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("key_money"));
        tblRooms.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));

        initUI();
        addPattern();

        tblRooms.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            btnSave.setText(newValue != null ? "Update" : "Save");
            btnSave.setDisable(newValue == null);

            if (newValue != null) {
                txtRoomID.setText(newValue.getRoom_type_id());
                txtRoomID.setEditable(false);
                txtType.setText(newValue.getType());
                txtKeyMoney.setText(String.valueOf(newValue.getKey_money()));
                txtRoomsQty.setText(String.valueOf(newValue.getQty()));

                txtRoomID.setDisable(false);
                txtType.setDisable(false);
                txtKeyMoney.setDisable(false);
                txtRoomsQty.setDisable(false);
            }
        });
        txtRoomsQty.setOnAction(event -> btnSave.fire());
        loadAllRooms();
    }

    private void loadAllRooms() {
        tblRooms.getItems().clear();
        /*Get all Rooms*/
        try {
            ArrayList<RoomDTO> allRooms = roomBo.getAllRooms();
            for (RoomDTO room : allRooms) {
                tblRooms.getItems().add(new RoomTM(room.getRoom_type_id(), room.getType(), room.getKey_money(), room.getQty()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void initUI() {
        txtRoomID.clear();
        txtType.clear();
        txtKeyMoney.clear();
        txtRoomsQty.clear();
        txtSearch.clear();
        txtRoomID.setDisable(true);
        txtType.setDisable(true);
        txtKeyMoney.setDisable(true);
        txtRoomsQty.setDisable(true);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    public void btnAddNewOnAction(ActionEvent actionEvent) {
        txtRoomID.setDisable(false);
        txtRoomID.setEditable(true);
        txtType.setDisable(false);
        txtKeyMoney.setDisable(false);
        txtRoomsQty.setDisable(false);
        txtRoomID.clear();
        txtRoomID.setText("RM-");
        txtType.clear();
        txtKeyMoney.clear();
        txtRoomsQty.clear();
        txtSearch.clear();
        txtRoomID.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblRooms.getSelectionModel().clearSelection();
    }

    private void addPattern() {
        Pattern patternId = Pattern.compile("^RM-[0-9]{4}$");
        Pattern patternType = Pattern.compile("^[A-z-/ ]{3,50}$");
        Pattern patternKeyMoney = Pattern.compile("^[0-9.]{4,10}$");
        Pattern patternQty = Pattern.compile("^[0-9]{1,2}$");

        map.put(txtRoomID, patternId);
        map.put(txtType, patternType);
        map.put(txtKeyMoney, patternKeyMoney);
        map.put(txtRoomsQty, patternQty);
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
        String id = txtRoomID.getText();
        String type = txtType.getText();
        String key_money = txtKeyMoney.getText();
        String qty = txtRoomsQty.getText();

        if (!id.matches("^RM-[0-9]{4}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid room id").show();
            txtRoomID.requestFocus();
            return;
        } else if (!type.matches("^[A-z-/ ]{3,50}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid type").show();
            txtType.requestFocus();
            return;
        } else if (!key_money.matches("^[0-9.]{4,10}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid key money").show();
            txtKeyMoney.requestFocus();
            return;
        } else if (!qty.matches("^[0-9]{1,2}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid rooms quantity").show();
            txtRoomsQty.requestFocus();
            return;
        }

        if (btnSave.getText().equalsIgnoreCase("Save")) {
            /*Save Room*/
            try {
                if (existRoom(id)) {
                    new Alert(Alert.AlertType.ERROR, id + " already exists").show();
                }

                roomBo.saveRoom(new RoomDTO(id, type, key_money, Integer.parseInt(qty)));
                tblRooms.getItems().add(new RoomTM(id, type, key_money, Integer.parseInt(qty)));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save the room " + e.getMessage()).show();
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            /*Update Room*/
            try {
                if (!existRoom(id)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such room associated with the id " + id).show();
                }
                //Room update
                roomBo.updateRoom(new RoomDTO(id, type, key_money, Integer.parseInt(qty)));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the room " + id + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            RoomTM selectedRoom = tblRooms.getSelectionModel().getSelectedItem();
            selectedRoom.setType(type);
            selectedRoom.setKey_money(key_money);
            selectedRoom.setQty(Integer.parseInt(qty));
            tblRooms.refresh();
        }
        btnAddNew.fire();
    }

    boolean existRoom(String id) throws SQLException, ClassNotFoundException {
        return roomBo.roomExist(id);
    }

    public void btnDeleteOnAction() {
        /*Delete Room*/
        String id = tblRooms.getSelectionModel().getSelectedItem().getRoom_type_id();
        try {
            if (!existRoom(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such room associated with the id " + id).show();
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this room?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)) {
                roomBo.deleteRoom(id);
                tblRooms.getItems().remove(tblRooms.getSelectionModel().getSelectedItem());
                tblRooms.getSelectionModel().clearSelection();
                initUI();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the room " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void findRoom(String id) {
        tblRooms.getItems().clear();
        try {
            RoomDTO room = roomBo.searchRoom(id);
            if (room != null) {
                tblRooms.getItems().add(new RoomTM(room.getRoom_type_id(), room.getType(), room.getKey_money(), room.getQty()));
            } else {
                new Alert(Alert.AlertType.INFORMATION, "No search results.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find the room " + id, e);
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
        loadAllRooms();
    }
}
