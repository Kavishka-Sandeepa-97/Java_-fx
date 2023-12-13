package dto.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderTm extends RecursiveTreeObject<OrderTm> {
    private String itemCode;
    private String desc ;
    private int qty;
    private double amount;
    private JFXButton btn;
}
