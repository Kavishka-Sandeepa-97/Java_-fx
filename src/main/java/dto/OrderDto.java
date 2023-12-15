package dto;

import dto.OrderDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {

    private String OrderId;
    private String date;
    private String customerId;
    private List<OrderDetailsDto> list;

}
