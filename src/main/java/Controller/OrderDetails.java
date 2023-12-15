package Controller;

import dto.OrderDto;
import dto.tm.CustomerTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.OrderModel;
import model.impl.OrderModelImpl;

import java.sql.SQLException;

public class OrderDetails {

    @FXML
    private TableView<OrderDto> tblAllOrders;

    @FXML
    private TableColumn colOrderID;

    @FXML
    private TableColumn cilDate;

    @FXML
    private TableColumn colCustomerId;

    @FXML
    private TableView tblOrderDetails;

    @FXML
    private TableColumn colItemID;

    @FXML
    private TableColumn colQTY;

    @FXML
    private TableColumn colUnitePrice;
    OrderModel orderModel=new OrderModelImpl();
    public void initialize() {


        loadCustomerTable();
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        cilDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            setData((CustomerTm) newValue);//we can use genarics above tblCustomer <CustomerTM>
        });
    }

    private void loadCustomerTable() {
        ObservableList<OrderDto> tmList = FXCollections.observableArrayList();
        try {

            for(OrderDto orderDto: orderModel.allOrders()){
                tmList.add(orderDto);
            }
            tblAllOrders.setItems(tmList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void setData(CustomerTm newValue) {

    }

}
