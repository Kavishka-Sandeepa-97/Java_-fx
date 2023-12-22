package model.impl;

import db.DBConnection;
import dto.OrderDetailsDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.OrderDetailsModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailModelImpl implements OrderDetailsModel {
    @Override
    public boolean saveOrderDetails(List<OrderDetailsDto> list) throws SQLException, ClassNotFoundException {
        boolean isDetailsSaved=true;
        for(OrderDetailsDto dto:list){
            String sql="insert into orderDetail Values(?,?,?,?)";
            PreparedStatement pstm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            pstm.setString(1, dto.getOrderID());
            pstm.setString(2, dto.getItemCode());
            pstm.setInt(3, dto.getOrderQTY());
            pstm.setDouble(4, dto.getUnitePrice());

            if (!(pstm.executeUpdate()>0)){
                isDetailsSaved=false;
            }
        }
        return isDetailsSaved;

    }

    @Override
    public ObservableList<OrderDetailsDto> getOrderDetail(String orderId) throws SQLException, ClassNotFoundException {


        ObservableList<OrderDetailsDto> list= FXCollections.observableArrayList();
        String sql="select * from orderdetail where orderId=?";
        Connection conn=DBConnection.getInstance().getConnection();

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1,orderId);
        ResultSet rst=preparedStatement.executeQuery();
        while (rst.next()){
            list.add(new OrderDetailsDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getDouble(4)
            ));

        }
        return list;
    }
}
