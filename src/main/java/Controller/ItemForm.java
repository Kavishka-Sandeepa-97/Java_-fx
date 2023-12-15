package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import db.DBConnection;
import dto.ItemDto;
import dto.tm.CustomerTm;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import dto.tm.ItemTm;
import model.ItemModel;
import model.impl.ItemModelImpl;

import java.io.IOException;
import java.sql.*;
import java.util.function.Predicate;

public class ItemForm {

    @FXML
    private GridPane pane;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtItemCode;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtQty;
    @FXML

    private JFXTreeTableView< ItemTm> treeTableView;

    @FXML
    private TreeTableColumn colItemCode;

    @FXML
    private TreeTableColumn colDescription;

    @FXML
    private TreeTableColumn colUnitPrice;

    @FXML
    private TreeTableColumn colQty;

    @FXML
    private TreeTableColumn colOption;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private Button backButton;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;
    private ItemModel itemModel=new ItemModelImpl();


    @FXML
  private void initialize() {
        loadItemTable();
        colItemCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colUnitPrice.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitePrice"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
                treeTableView.setPredicate(new Predicate<TreeItem<ItemTm>>() {
                    @Override
                    public boolean test(TreeItem<ItemTm> treeItem) {
                        return treeItem.getValue().getItemCode().contains(newValue) || treeItem.getValue().getDesc().contains(newValue);
                    }
                });
            }
        });

          treeTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
             setData(newValue);
          });


    }
    private void setData(TreeItem<ItemTm> newValue) {
        if (newValue != null) {
            ItemTm selectedItem = newValue.getValue();

            // Now you can access the properties of the selected item
            txtItemCode.setText( selectedItem.getItemCode());
            txtDescription.setText( selectedItem.getDesc());
            txtPrice.setText(selectedItem.getUnitePrice()+"");
            txtQty.setText(selectedItem.getQty()+"");
        }
    }

    public void saveButtonOnAction(javafx.event.ActionEvent actionEvent) {

        boolean isSaved = false;
        try {
            isSaved = itemModel.saveItem(new ItemDto(
                   txtItemCode.getText(),
                   txtDescription.getText(),
                   Double.parseDouble(txtPrice.getText()),
                   Integer.parseInt(txtQty.getText())

           ));
        } catch (SQLIntegrityConstraintViolationException ex) {
            new Alert(Alert.AlertType.INFORMATION, "Duplicate Entry").show();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Item Saved").show();
                loadItemTable();
                clearField();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Item Not Saved").show();
            }

        }


    private void clearField() {
        treeTableView.refresh();//this is not usabale
        txtItemCode.clear();
        txtDescription.clear();
        txtQty.clear();
        txtPrice.clear();
    }

    private void loadItemTable() {
        String sql = "select * from Item";
        ObservableList<ItemTm> tmList = FXCollections.observableArrayList();

        try {

            Connection conn = DBConnection.getInstance().getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                JFXButton btn = new JFXButton("Delete");
                ItemTm tm = new ItemTm(res.getString(1),
                        res.getString(2),
                        res.getDouble(3),
                        res.getInt(4),
                        btn
                );
                btn.setOnAction((actionEvent) -> {
                    deleteItem(txtItemCode.getText());
                });
                tmList.add(tm);

            }
            //check
            TreeItem<ItemTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
            treeTableView.setRoot(treeItem);
            treeTableView.setShowRoot(false);

           // treeTableView.setItems(tmList);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void deleteItem(String id) {
        boolean isDelete= false;
        try {
            isDelete = itemModel.deleteItem(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (isDelete) {
                new Alert(Alert.AlertType.INFORMATION, "Item Deleted").show();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Item Not Deleted").show();
            }
            loadItemTable();
            clearField();

    }


    public void backButtonOnAction(javafx.event.ActionEvent actionEvent) {

        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateButtonOnAction(javafx.event.ActionEvent actionEvent) {
        boolean isUpdate = false;
        try {
            isUpdate = itemModel.updateItem(new ItemDto(
                    txtItemCode.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQty.getText())
            ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (isUpdate){
            new Alert(Alert.AlertType.INFORMATION,"Update Successfully");
            clearField();
            loadItemTable();
        }else {  new Alert(Alert.AlertType.INFORMATION,"Not Updated");}
    }
}
