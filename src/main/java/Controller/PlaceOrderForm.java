package Controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import db.DBConnection;
import dto.CustomerDto;
import dto.ItemDto;
import dto.OrderDetailsDto;
import dto.OrderDto;
import dto.tm.OrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import model.CustomerModel;
import model.ItemModel;
import model.OrderModel;
import model.impl.CustomerImpl;
import model.impl.ItemModelImpl;
import model.impl.OrderModelImpl;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderForm {

    public TreeTableColumn colItemCode;
    public TreeTableColumn colDescription;
    public TreeTableColumn colQty;
    public TreeTableColumn colAmount;
    public TreeTableColumn colOption;
    public Button btnAddToCart;
    public Button btnPlaceOrder;
    public JFXTreeTableView<OrderTm> treeTable;
    public Label lblTotalAmount;
    public Label lblOrderId;
    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtDesc;
    @FXML
    private JFXTextField txtUnitePrice;
    @FXML
    private JFXTextField txtQty;
    @FXML
    private Button backButton;
    @FXML
    private JFXComboBox cmbCustomerId;
    @FXML
    private JFXComboBox cmbItemCode;
    private CustomerModel customerModel;
    private ItemModel itemModel;
    private List<CustomerDto> custlist;
    private List<ItemDto> itemlist;
    private ObservableList<OrderTm> tmOrderList = FXCollections.observableArrayList();
    private double totalAmount = 0.00;
    private OrderModel orderModel = new OrderModelImpl();

    public void initialize() {

        colItemCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("amount"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        customerModel = new CustomerImpl();
        itemModel = new ItemModelImpl();
        loadCustomerIds();
        loadItemeCodes();
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            for (CustomerDto dto : custlist) {
                if (dto.getId().equals(newValue)) {
                    txtName.setText(dto.getName());
                    return;
                }
                ;
            }
        });
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            for (ItemDto dto : itemlist) {
                if (dto.getItemCode().equals(newValue)) {
                    txtDesc.setText(dto.getDesc());
                    txtUnitePrice.setText(String.format("%.2f", dto.getUnitePrice()));
                    return;
                }
                ;
            }
        });
        orderIdGenarate();

    }

    private void loadCustomerIds() {
        ObservableList<String> dto = FXCollections.observableArrayList();
        try {
            custlist = customerModel.allCustomer();
            for (CustomerDto x : custlist) {
                dto.add(x.getId());
            }
            cmbCustomerId.setItems(dto);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadItemeCodes() {

        ObservableList<String> dto = FXCollections.observableArrayList();
        try {
            itemlist = itemModel.allItem();
            for (ItemDto x : itemlist) {
                dto.add(x.getItemCode());
            }
            cmbItemCode.setItems(dto);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void backButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) cmbCustomerId.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToCartButtonOnAction(ActionEvent actionEvent) {

        String id = cmbItemCode.getValue().toString();
        try {
            int qtyOnHand = itemModel.getItem(id).getQty();

            if (qtyOnHand < Integer.parseInt(txtQty.getText())) {
                new Alert(Alert.AlertType.ERROR, "Insuficent Quantity").show();
                return;
            }
            if (Integer.parseInt(txtQty.getText()) < 1) {
                new Alert(Alert.AlertType.ERROR, "Input Valid Quantity").show();
                return;
            }
            double amount = Integer.parseInt(txtQty.getText()) * Double.parseDouble(txtUnitePrice.getText());

            JFXButton btn = new JFXButton("Delete");
            OrderTm tm = new OrderTm(
                    cmbItemCode.getValue().toString(),
                    txtDesc.getText(),
                    Integer.parseInt(txtQty.getText()),
                    amount,
                    btn
            );
            btn.setOnAction((actionEvent1) -> {
                tmOrderList.remove(tm);
                totalAmount -= tm.getAmount();
                lblTotalAmount.setText(totalAmount + "");
            });
            boolean isExist = false;

            for (OrderTm orderTm : tmOrderList) {
                if (tm.getItemCode().equals(orderTm.getItemCode())) {
                    if (orderTm.getQty() + tm.getQty() > qtyOnHand) {
                        new Alert(Alert.AlertType.ERROR, "Insuficent Quantity").show();
                        return;
                    }
                    orderTm.setQty(orderTm.getQty() + tm.getQty());//???????**************
                    orderTm.setAmount(orderTm.getAmount() + tm.getAmount());//?????**********
                    isExist = true;

                }
            }

            if (!isExist) {
                tmOrderList.add(tm);
                totalAmount += tm.getAmount();
            }

            lblTotalAmount.setText(totalAmount + "");

            TreeItem<OrderTm> treeItem = new RecursiveTreeItem<OrderTm>(tmOrderList, RecursiveTreeObject::getChildren);
            treeTable.setRoot(treeItem);
            treeTable.setShowRoot(false);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException exception) {
            new Alert(Alert.AlertType.ERROR, "Input Valid  Quantity").show();
        }

    }

    public void orderIdGenarate() {
        try {
            OrderDto orderDto = orderModel.lastOrder();
            if (orderDto == null) {
                lblOrderId.setText("D001");
            } else {
                String string = orderDto.getOrderId();
                int id = Integer.parseInt(string.split("D")[1]);
                id++;
                lblOrderId.setText(String.format("D%03d", id));
            }

        } catch (SQLException e) {
            ;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    public void placeOrderButtonOnAction(ActionEvent actionEvent){
        List<OrderDetailsDto> list=new ArrayList<>();
        for (OrderTm tm:tmOrderList){
                list.add( new OrderDetailsDto(
                        lblOrderId.getText(),
                        tm.getItemCode(),
                        tm.getQty(),
                        tm.getAmount()/tm.getQty()
                ));
        }
 //       if (!tmOrderList.isEmpty()) {
            boolean isSaved = false;
            try {
                isSaved = orderModel.saveOrder(
                        new OrderDto(
                                lblOrderId.getText(),
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("YY-MM-dd")),
                                cmbCustomerId.getValue().toString(),
                                list
                        )
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(isSaved){
                new Alert(Alert.AlertType.INFORMATION,"OrderSaved").show();
            }
     //   }
        else { new Alert(Alert.AlertType.INFORMATION,"Some Error Here").show();}
   }
}

