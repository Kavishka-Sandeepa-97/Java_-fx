package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import db.DBConnection;
import dto.tm.CustomerTm;
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

import java.io.IOException;
import java.sql.*;

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

    @FXML
  private void initialize(){
        loadItemTable();
        colItemCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colUnitPrice.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitePrice"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

    }
    public void saveButtonOnAction(javafx.event.ActionEvent actionEvent) {

        String sql = "insert into item values(?,?,?,?)";


        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql);

            pstm.setString(1,txtItemCode.getText());
            pstm.setString(2,txtDescription.getText());
            pstm.setDouble(3,Double.parseDouble(txtPrice.getText()));
            pstm.setInt(4,Integer.parseInt(txtQty.getText()));

            int res = pstm.executeUpdate();
            if (res > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Item Saved").show();
                loadItemTable();
                clearField();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Item Not Saved").show();
            }

        } catch (SQLIntegrityConstraintViolationException ex) {
            new Alert(Alert.AlertType.INFORMATION, "Duplicate Entry").show();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
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
                    deleteItem(tm.getItemCode());
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
        String sql = "delete  from Item  where  itemCode= ?";

        try {

            Connection conn =DBConnection.getInstance().getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1,id);
            int res = pstm.executeUpdate();
            if (res > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Item Deleted").show();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Item Not Deleted").show();
            }
            loadItemTable();
            clearField();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
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

    }
}
