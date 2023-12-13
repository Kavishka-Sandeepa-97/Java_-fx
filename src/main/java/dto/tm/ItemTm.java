package dto.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemTm extends RecursiveTreeObject<ItemTm> {
   private String itemCode;
    private String desc;
    private double unitePrice;
    private int qty;
    private JFXButton btn;

    public ItemTm(String itemCode, String desc, double unitePrice, int qty){
        this.itemCode = itemCode;
        this.desc = desc;
        this.unitePrice = unitePrice;
        this.qty = qty;
    }
}
