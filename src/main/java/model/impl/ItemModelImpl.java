package model.impl;

import db.DBConnection;
import dto.ItemDto;
import javafx.scene.control.Alert;
import model.ItemModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemModelImpl implements ItemModel {

    @Override
    public boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException {

              String sql = "insert into item values(?,?,?,?)";

            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql);

            pstm.setString(1,dto.getItemCode());
            pstm.setString(2,dto.getDesc());
            pstm.setDouble(3,dto.getUnitePrice());
            pstm.setInt(4,dto.getQty());

            int res = pstm.executeUpdate();
            if (res>0){
                return true;
            }
            return false;
    }


    @Override
    public boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Item SET Description=?, UnitPrice=?, qtyOnHand=? WHERE itemCode=?";
            Connection conn =DBConnection.getInstance().getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1,dto.getDesc());
            pstm.setString(2,String.valueOf(dto.getUnitePrice()));
            pstm.setString(3,String.valueOf(dto.getQty()));
            pstm.setString(4, dto.getItemCode());
            int res = pstm.executeUpdate();
            if (res > 0) {
                return true;
            }
                return false;

    }

    @Override
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException {
        String sql = "delete  from Item  where  itemCode= ?";

        Connection conn =DBConnection.getInstance().getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1,id);
        int res = pstm.executeUpdate();
        if(res>0) return true;
        return false;
    }

    @Override
    public List<ItemDto> allItem() throws SQLException, ClassNotFoundException {
        List<ItemDto> list=new ArrayList<>();
        String sql="select*from Item";
        Connection conn= DBConnection.getInstance().getConnection();
        PreparedStatement pstm= conn.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
        list.add(new ItemDto(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getDouble(3),
                resultSet.getInt(4)
        ));
            }
        return list;
        }

    @Override
    public ItemDto getItem(String id) throws SQLException, ClassNotFoundException {
        String sql="select * from item where itemCode=?";
        Connection conn=DBConnection.getInstance().getConnection();
        PreparedStatement pstm=conn.prepareStatement(sql);
        pstm.setString(1,id);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
           return new ItemDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        }
        return null;
    }
}
