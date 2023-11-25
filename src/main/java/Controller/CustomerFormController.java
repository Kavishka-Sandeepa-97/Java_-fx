package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerFormController {

    public void initialize() {

    }

    @FXML
    private TextField textID;

    @FXML
    private TextField textName;

    @FXML
    private TextField textAddress;

    @FXML
    private TextField textSalary;

    @FXML
    private TableView<?> tblCustomer;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colSalary;

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colOption;

    @FXML
    private Button btnReload;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnSave;

    @FXML
    void reloadButtonOnAction(ActionEvent event) {

    }

    @FXML
    void saveButtonOnAction(ActionEvent event) {

    }

    @FXML
    void updateButtonOnAction(ActionEvent event) {

    }


    public void reloadButtonOnAction(javafx.event.ActionEvent actionEvent) {
    }

    public void updateButtonOnAction(javafx.event.ActionEvent actionEvent) {
    }

    public void saveButtonOnAction(javafx.event.ActionEvent actionEvent) {
    }
}
