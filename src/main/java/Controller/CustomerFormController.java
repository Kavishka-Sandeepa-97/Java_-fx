package Controller;

import db.DBConnection;
import dto.CustomerDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import dto.tm.CustomerTm;
import model.CustomerModel;
import model.impl.CustomerImpl;

import java.io.IOException;
import java.sql.*;

import static java.lang.Class.forName;

public class CustomerFormController {


    @FXML
    private TextField textID;

    @FXML
    private TextField textName;

    @FXML
    private TextField textAddress;

    @FXML
    private TextField textSalary;

    @FXML
    private TableView tblCustomer;

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

    private CustomerModel customerModel=new CustomerImpl();

    public void initialize() {


        loadCustomerTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            setData((CustomerTm) newValue);//we can use genarics above tblCustomer <CustomerTM>
        });
    }

    private void setData(CustomerTm newValue) {
        if (newValue != null) {
            textID.setEditable(false);
            textID.setText(newValue.getId());
            textName.setText(newValue.getName());
            textAddress.setText(newValue.getAddress());
            textSalary.setText(String.valueOf(newValue.getSalary()));
        }
    }

    private void loadCustomerTable() {

        ObservableList<CustomerTm> tmList = FXCollections.observableArrayList();

        try {
            for (CustomerDto dto : customerModel.allCustomer()) {
                Button btn=new Button("delete");
                CustomerTm tm=new CustomerTm(
                        dto.getId(),
                        dto.getName(),
                        dto.getAddress(),
                        dto.getSalary(),
                        btn
                );
                btn.setOnAction(actionEvent ->
                        deleteCustomer(dto.getId()));
                tmList.add(tm);
            }
            tblCustomer.setItems(tmList);
        } catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
        tblCustomer.setItems(tmList);
    }

    private void deleteCustomer(String id) {

        boolean res= false;
        try {
            res = customerModel.deleteCustomer(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        if (res) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Deleted").show();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Customer Not Deleted").show();
            }

    }

    public void reloadButtonOnAction(javafx.event.ActionEvent actionEvent) {
        loadCustomerTable();
        clearField();
    }

    private void clearField() {
        tblCustomer.refresh();//this is not usabale
        textID.clear();
        textName.clear();
        textAddress.clear();
        textSalary.clear();
    }

    public void updateButtonOnAction(javafx.event.ActionEvent actionEvent) {
        boolean res;
        try {
             res = customerModel.updateCustomer(new CustomerDto(
                    textID.getText(),
                    textName.getText(),
                    textAddress.getText(),
                    Double.parseDouble(textSalary.getText())
            ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(res){
                new Alert(Alert.AlertType.INFORMATION, "Customer Updated").show();
                loadCustomerTable();
                clearField();

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Not Updated").show();
            }

    }

    public void saveButtonOnAction(javafx.event.ActionEvent actionEvent) {

        try {
            boolean isSaved=customerModel.saveCustomer(new CustomerDto(
                    textID.getText(), textName.getText(),textAddress.getText(),Double.parseDouble(textSalary.getText())
            ));
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Saved").show();
                loadCustomerTable();
                clearField();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Not Saved").show();
            }

        } catch (SQLIntegrityConstraintViolationException ex) {
            new Alert(Alert.AlertType.INFORMATION, "Duplicate Entry").show();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void backButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) textID.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



