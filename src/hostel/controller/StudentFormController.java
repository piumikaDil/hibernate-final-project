package hostel.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import hostel.bo.BOFactory;
import hostel.bo.custom.StudentBO;
import hostel.dto.StudentDTO;
import hostel.validation.Validations;
import hostel.view.tdm.StudentTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.ENTER;

public class StudentFormController {
    private final StudentBO studentBO = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);
    public JFXTextField txtSearch;
    public JFXButton btnSearch;
    public JFXButton btnRefresh;
    public JFXTextField txtStID;
    public JFXTextField txtStName;
    public JFXTextField txtStAddress;
    public JFXTextField txtConNo;
    public JFXTextField txtDoB;
    public JFXComboBox<String> cmbGender;
    public JFXButton btnAddNew;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public TableView<StudentTM> tblStudents;
    public LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    private StackPane stackPane;
    private AnchorPane rootPane;

    public void setPane(StackPane stackPane, AnchorPane rootPane) {
        this.stackPane = stackPane;
        this.rootPane = rootPane;
    }

    public void initialize() {
        tblStudents.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("student_id"));
        tblStudents.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudents.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudents.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact_no"));
        tblStudents.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("dob"));
        tblStudents.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("gender"));

        initUI();
        addPattern();
        List<String> list = new ArrayList<>();
        list.add("Male");
        list.add("Female");
        ObservableList<String> obList = FXCollections.observableList(list);
        cmbGender.getItems().clear();
        cmbGender.setItems(obList);

        tblStudents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            btnSave.setText(newValue != null ? "Update" : "Save");
            btnSave.setDisable(newValue == null);

            if (newValue != null) {
                txtStID.setText(newValue.getStudent_id());
                txtStID.setEditable(false);
                txtStName.setText(newValue.getName());
                txtStAddress.setText(newValue.getAddress());
                txtConNo.setText(newValue.getContact_no());
                txtDoB.setText(String.valueOf(newValue.getDob()));
                cmbGender.setValue(newValue.getGender());

                txtStID.setDisable(false);
                txtStName.setDisable(false);
                txtStAddress.setDisable(false);
                txtConNo.setDisable(false);
                txtDoB.setDisable(false);
                cmbGender.setDisable(false);
            }
        });
        loadAllStudents();
    }

    private void loadAllStudents() {
        tblStudents.getItems().clear();
        /*Get all Students*/
        try {
            ArrayList<StudentDTO> allStudents = studentBO.getAllStudents();
            for (StudentDTO student : allStudents) {
                tblStudents.getItems().add(new StudentTM(student.getStudent_id(), student.getName(), student.getAddress(), student.getContact_no(), student.getDob(), student.getGender()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void initUI() {
        txtStID.clear();
        txtStName.clear();
        txtStAddress.clear();
        txtConNo.clear();
        txtDoB.clear();
        cmbGender.getSelectionModel().clearSelection();
        txtSearch.clear();
        txtStID.setDisable(true);
        txtStName.setDisable(true);
        txtStAddress.setDisable(true);
        txtConNo.setDisable(true);
        txtDoB.setDisable(true);
        cmbGender.setDisable(true);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    public void btnAddNewOnAction(ActionEvent actionEvent) {
        txtStID.setDisable(false);
        txtStID.setEditable(true);
        txtStName.setDisable(false);
        txtStAddress.setDisable(false);
        txtConNo.setDisable(false);
        txtDoB.setDisable(false);
        cmbGender.setDisable(false);
        txtStID.clear();
        txtStName.clear();
        txtStAddress.clear();
        txtConNo.clear();
        txtDoB.clear();
        cmbGender.getSelectionModel().clearSelection();
        txtSearch.clear();
        txtStID.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblStudents.getSelectionModel().clearSelection();
    }

    private void addPattern() {
        Pattern patternId = Pattern.compile("^[A-z0-9-/ ]{3,30}$");
        Pattern patternName = Pattern.compile("^[A-z. ]{3,30}$");
        Pattern patternAddress = Pattern.compile("^[A-z0-9 ,/]{4,30}$");
        Pattern patternContact = Pattern.compile("^(?:7|0|(?:\\+94))[0-9]{9,10}$");
        Pattern patternDoB = Pattern.compile("^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");

        map.put(txtStID, patternId);
        map.put(txtStName, patternName);
        map.put(txtStAddress, patternAddress);
        map.put(txtConNo, patternContact);
        map.put(txtDoB, patternDoB);
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
        String id = txtStID.getText();
        String name = txtStName.getText();
        String address = txtStAddress.getText();
        String contact = txtConNo.getText();
        String dob = txtDoB.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateOfBirth = LocalDate.parse(dob, formatter);
        String gender = cmbGender.getValue();

        if (!id.matches("^[A-z0-9-/ ]{3,30}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid student id").show();
            txtStID.requestFocus();
            return;
        } else if (!name.matches("^[A-z. ]{3,30}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid name").show();
            txtStName.requestFocus();
            return;
        } else if (!address.matches("^[A-z0-9 ,/]{4,30}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid address").show();
            txtStAddress.requestFocus();
            return;
        } else if (!contact.matches("^(?:7|0|(?:\\+94))[0-9]{9,10}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid contact no.").show();
            txtConNo.requestFocus();
            return;
        } else if (!dob.matches("^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid date of birth").show();
            txtDoB.requestFocus();
            return;
        }

        if (btnSave.getText().equalsIgnoreCase("Save")) {
            /*Save Student*/
            try {
                if (existStudent(id)) {
                    new Alert(Alert.AlertType.ERROR, id + " already exists").show();
                }

                studentBO.saveStudent(new StudentDTO(id, name, address, contact, dateOfBirth, gender));
                tblStudents.getItems().add(new StudentTM(id, name, address, contact, dateOfBirth, gender));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save the student " + e.getMessage()).show();
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            /*Update Student*/
            try {
                if (!existStudent(id)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such student associated with the id " + id).show();
                }
                //Student update
                studentBO.updateStudent(new StudentDTO(id, name, address, contact, dateOfBirth, gender));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the student " + id + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            StudentTM selectedStudent = tblStudents.getSelectionModel().getSelectedItem();
            selectedStudent.setName(name);
            selectedStudent.setAddress(address);
            selectedStudent.setContact_no(contact);
            selectedStudent.setDob(dateOfBirth);
            selectedStudent.setGender(gender);
            tblStudents.refresh();
        }
        btnAddNew.fire();
    }

    boolean existStudent(String id) throws SQLException, ClassNotFoundException {
        return studentBO.studentExist(id);
    }

    public void btnDeleteOnAction() {
        /*Delete Student*/
        String id = tblStudents.getSelectionModel().getSelectedItem().getStudent_id();
        try {
            if (!existStudent(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such Student associated with the id " + id).show();
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this student?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)) {
                studentBO.deleteStudent(id);
                tblStudents.getItems().remove(tblStudents.getSelectionModel().getSelectedItem());
                tblStudents.getSelectionModel().clearSelection();
                initUI();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the student " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void findStudent(String id) {
        tblStudents.getItems().clear();
        try {
            StudentDTO student = studentBO.searchStudent(id);
            if (student != null) {
                tblStudents.getItems().add(new StudentTM(student.getStudent_id(), student.getName(), student.getAddress(), student.getContact_no(), student.getDob(), student.getGender()));
            } else {
                new Alert(Alert.AlertType.INFORMATION, "No search results.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find the student " + id, e);
        }
    }

    public void menuDeleteOnAction(ActionEvent event) {
        btnDeleteOnAction();
    }

    public void txtSearchKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            findStudent(txtSearch.getText());
    }

    public void btnSearchOnAction(ActionEvent event) {
        findStudent(txtSearch.getText());
    }

    public void btnRefreshOnAction(ActionEvent event) {
        initUI();
        loadAllStudents();
    }
}
