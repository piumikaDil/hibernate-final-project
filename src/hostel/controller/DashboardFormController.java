package hostel.controller;

import com.jfoenix.controls.*;
import hostel.bo.BOFactory;
import hostel.bo.custom.UserBO;
import hostel.dto.RoomDTO;
import hostel.dto.UserDTO;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.sql.SQLException;
import java.util.ArrayList;

public class DashboardFormController {
    public JFXComboBox<String> cmbRoomID;
    public JFXTextField txtType;
    public JFXTextField txtKeyMoney;
    public Label lblAvaRooms;
    public Label lblResRooms;
    public JFXButton btnAddNew;
    public Label lblDesc;
    public JFXTextField txtUsername;
    public JFXPasswordField txtPassword;
    public JFXTextField txtShowPwd;
    public JFXCheckBox chBxPassword;
    public JFXButton btnVerify;
    public JFXComboBox<String> cmbUserID;
    public JFXButton btnCancel;
    public JFXButton btnDelete;
    UserBO userBO = (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);
    private StackPane stackPane;
    private AnchorPane rootPane;

    public void setPane(StackPane stackPane, AnchorPane rootPane) {
        this.stackPane = stackPane;
        this.rootPane = rootPane;
    }

    public void initialize() {
        txtShowPwd.setManaged(false);
        txtShowPwd.setVisible(false);

        txtShowPwd.managedProperty().bind(chBxPassword.selectedProperty());
        txtShowPwd.visibleProperty().bind(chBxPassword.selectedProperty());

        txtPassword.managedProperty().bind(chBxPassword.selectedProperty().not());
        txtPassword.visibleProperty().bind(chBxPassword.selectedProperty().not());

        txtShowPwd.textProperty().bindBidirectional(txtPassword.textProperty());

        cmbRoomID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    /*Search Room*/
                    try {
                        if (!existRoom(newValue + "")) {
                            new Alert(Alert.AlertType.ERROR, "There is no such room associated with the id " + newValue + "").show();
                        }

                        RoomDTO search = userBO.searchRoom(newValue + "");
                        txtType.setText(search.getType());
                        txtKeyMoney.setText(search.getKey_money());
                        availableAndReservedRooms(cmbRoomID.getValue(), search.getQty());
                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to find the room " + newValue + "" + e).show();
                    }
                } catch (ClassNotFoundException throwable) {
                    throwable.printStackTrace();
                }
            } else {
                txtType.clear();
                txtKeyMoney.clear();
            }
        });

        cmbUserID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            txtUsername.setEditable(newValue != null);
            txtPassword.setEditable(newValue != null);
            btnVerify.setDisable(newValue == null);

            if (newValue != null) {

                /*Find User*/
                try {
                    if (btnVerify.getText().equalsIgnoreCase("Verify")) {
                        //Search User
                        UserDTO search = userBO.searchUser(newValue + "");
                        if (search != null) txtUsername.setText(search.getUsername());
                    }
                } catch (SQLException | ClassNotFoundException throwable) {
                    throwable.printStackTrace();
                }
            } else {
                txtUsername.clear();
                txtPassword.clear();
            }
        });
        loadAllRoomIds();
        loadAllUserIds();
        btnVerify.setDisable(true);
    }

    public void availableAndReservedRooms(String roomId, int total) {
        try {
            int availableRooms = total - userBO.totalReservedRooms(roomId) + userBO.availableRooms(roomId);
            lblAvaRooms.setText(String.valueOf(availableRooms));
            lblResRooms.setText(String.valueOf(total - availableRooms));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean existRoom(String id) throws SQLException, ClassNotFoundException {
        return userBO.checkRoomIsAvailable(id);
    }

    private void loadAllRoomIds() {
        try {
            ArrayList<RoomDTO> all = userBO.getAllRooms();
            for (RoomDTO roomDTO : all) {
                cmbRoomID.getItems().add(roomDTO.getRoom_type_id());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load room ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllUserIds() {
        try {
            ArrayList<UserDTO> all = userBO.getAllUsers();
            cmbUserID.getItems().clear();
            for (UserDTO userDTO : all) {
                cmbUserID.getItems().add(userDTO.getUser_id());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load user ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String generateNewUserId() {
        String id = "";
        try {
            id = userBO.generateNewUserID();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new id " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (id == null) {
            return "UID-001";
        }
        int newUserId = Integer.parseInt(id.replace("UID-", "")) + 1;
        return String.format("UID-%03d", newUserId);
    }

    public void btnAddNewOnAction(ActionEvent event) {
        btnVerify.setText("Save");
        lblDesc.setText("To add new user, enter the username and the password");
        txtUsername.clear();
        txtPassword.clear();
        txtUsername.setPromptText("Username");
        txtPassword.setPromptText("Password");
        txtShowPwd.setPromptText("Password");
        cmbUserID.setValue(generateNewUserId());
    }

    public boolean checkFieldsIsEmpty() {
        return cmbUserID.getValue().isEmpty() || txtPassword.getText().isEmpty() || txtUsername.getText().isEmpty();
    }

    public void btnVerifyOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!checkFieldsIsEmpty()) {
            if (btnVerify.getText().equalsIgnoreCase("Verify")) {
                UserDTO user = userBO.verifyUser(cmbUserID.getValue());
                if (user != null) {
                    if (user.getPassword().equals(txtPassword.getText())) {
                        new Alert(Alert.AlertType.INFORMATION, "Verification successful!").show();
                        changeUi();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Verification failed!").show();
                        lblDesc.setText("Re-enter your username and password");
                    }
                }
            } else if (btnVerify.getText().equalsIgnoreCase("Change")) {
                userBO.updateUser(new UserDTO(cmbUserID.getValue(), txtUsername.getText(), txtPassword.getText()));
                new Alert(Alert.AlertType.INFORMATION, "Username and password changed successfully").show();
                setUi();
            } else if (btnVerify.getText().equalsIgnoreCase("Save")) {
                try {
                    userBO.saveUser(new UserDTO(cmbUserID.getValue(), txtUsername.getText(), txtPassword.getText()));
                    new Alert(Alert.AlertType.INFORMATION, "User added successfully").show();
                    loadAllUserIds();
                    setUi();
                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, "User not added").show();
                    e.printStackTrace();
                }
            } else {
                try {
                    UserDTO user = userBO.verifyUser(cmbUserID.getValue());
                    if (user != null) {
                        if (user.getPassword().equals(txtPassword.getText())) {
                            userBO.deleteUser(cmbUserID.getValue());
                            new Alert(Alert.AlertType.INFORMATION, "Verification successful! User deleted successfully").show();
                            loadAllUserIds();
                            setUi();
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Verification failed! User not deleted").show();
                            lblDesc.setText("Re-enter your username and password");
                        }
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, "User not deleted").show();
                    e.printStackTrace();
                }
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Enter the username and the password").show();
        }
    }

    public void changeUi() {
        lblDesc.setText("Now, you can change your username and password");
        txtUsername.clear();
        txtPassword.clear();
        txtUsername.setPromptText("New username");
        txtPassword.setPromptText("New password");
        txtShowPwd.setPromptText("New password");
        btnVerify.setText("Change");
    }

    public void setUi() {
        lblDesc.setText("To change username and password, first verify it's you");
        cmbUserID.getSelectionModel().clearSelection();
        txtPassword.clear();
        txtUsername.setPromptText("Username");
        txtPassword.setPromptText("Password");
        txtShowPwd.setPromptText("Password");
        btnVerify.setText("Verify");
    }

    public void btnCancelOnAction(ActionEvent event) {
        setUi();
    }

    public void btnDeleteOnAction(ActionEvent event) {
        btnVerify.setText("Delete");
        lblDesc.setText("To delete a user, enter username and password");
        cmbUserID.getSelectionModel().clearSelection();
        txtPassword.clear();
        txtUsername.setPromptText("Username");
        txtPassword.setPromptText("Password");
        txtShowPwd.setPromptText("Password");
    }
}
