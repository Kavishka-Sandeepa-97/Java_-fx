package model;
import dto.ItemDto;
import java.sql.SQLException;
import java.util.List;

public interface ItemModel {
    boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    boolean updateItem(ItemDto dto);
    boolean deleteItem(String id);
    List<ItemDto> allItem() throws SQLException, ClassNotFoundException;
    ItemDto getItem(String id) throws SQLException, ClassNotFoundException;
}
