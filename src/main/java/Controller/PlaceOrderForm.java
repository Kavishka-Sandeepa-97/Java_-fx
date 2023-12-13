package Controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dto.CustomerDto;
import dto.ItemDto;
import dto.tm.ItemTm;
import dto.tm.OrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import model.CustomerModel;
import model.ItemModel;
import model.impl.CustomerImpl;
import model.impl.ItemModelImpl;

import java.io.IOException;
import java.sql.SQLException;
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
    private ObservableList<OrderTm> tmList= FXCollections.observableArrayList();

    public void initialize(){

        colItemCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("amount"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        customerModel=new CustomerImpl();
        itemModel=new ItemModelImpl();
        loadCustomerIds();
        loadItemeCodes();
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            for (CustomerDto dto:custlist){
                if(dto.getId().equals(newValue)){
                    txtName.setText(dto.getName());
                    return;
                };
            }
        });
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            for (ItemDto dto:itemlist){
                if(dto.getItemCode().equals(newValue)){
                    txtDesc.setText(dto.getDesc());
                    txtUnitePrice.setText(String.format("%.2f",dto.getUnitePrice()));
                    return;
                };
            }
        });



    }

    private void loadCustomerIds() {
        ObservableList<String> dto = FXCollections.observableArrayList();
        try {
             custlist = customerModel.allCustomer();
            for (CustomerDto x:custlist){
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
            for (ItemDto x:itemlist){
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


        String id=cmbItemCode.getValue().toString();
        try {
            int qtyOnHand= itemModel.getItem(id).getQty();
            if(qtyOnHand<Integer.parseInt(txtQty.getText())){
                new Alert(Alert.AlertType.ERROR,"Insuficent Quantity");
                return;
            }
            double amount=qtyOnHand*Double.parseDouble(txtUnitePrice.getText());
            OrderTm tm=new OrderTm(
                  cmbItemCode.getValue().toString(),
                  txtDesc.getText(),
                  Integer.parseInt(txtQty.getText()),
                  amount,
                  new JFXButton("Delete")
            );
            boolean isExist= false;
            for (OrderTm orderTm:tmList){
                if (tm.getItemCode().equals(orderTm.getItemCode())){
                    orderTm.setQty(orderTm.getQty()+ tm.getQty());//???????**************
                    orderTm.setAmount(orderTm.getAmount()+ tm.getAmount());//?????**********
                    isExist=true;
                }
            }
            if (!isExist){
                tmList.add(tm);
            }


            TreeItem<OrderTm> treeItem = new RecursiveTreeItem<OrderTm>(tmList, RecursiveTreeObject::getChildren);
            treeTable.setRoot(treeItem);
            treeTable.setShowRoot(false);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void placeOrderButtonOnAction(ActionEvent actionEvent) {

    }
}
