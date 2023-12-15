package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetailsDto{
    private String OrderID;
    private String ItemCode;
    private int OrderQTY;
    private double unitePrice;
}
