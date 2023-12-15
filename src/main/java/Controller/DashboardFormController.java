package Controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardFormController {

    public Label lblTime;
    @FXML
    private AnchorPane pane;
    public void initialize(){
        calculateTime();
    }

    private void calculateTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO,
                actionEvent -> lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                ,new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void customerButton(javafx.event.ActionEvent actionEvent) {
        Stage stage=(Stage)pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/CustomerForm.fxml"))));
            stage.show();
            stage.setTitle("Customer Form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void itemButtonOnAction(ActionEvent actionEvent) {
        Stage stage=(Stage)pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/ItemForm.fxml"))));
            stage.show();
            stage.setTitle("Item Form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void PlaceOrderButtonOnAction(ActionEvent actionEvent) {
        Stage stage=(Stage)pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/PlaceOrderForm.fxml"))));
            stage.show();
            stage.setTitle("Place Order Form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void orderDetailsOnAction(ActionEvent actionEvent) {
        Stage stage=(Stage)pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/OrderDetails.fxml"))));
            stage.show();
            stage.setTitle("Order Details Form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
