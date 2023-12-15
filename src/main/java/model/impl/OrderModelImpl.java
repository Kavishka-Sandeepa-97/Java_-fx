package model.impl;

import db.DBConnection;
import dto.OrderDto;
import model.OrderDetailsModel;
import model.OrderModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderModelImpl implements OrderModel {
    @Override
    public boolean saveOrder(OrderDto orderDto) throws SQLException {

        OrderDetailsModel orderDetailsModel=new OrderDetailModelImpl();
        Connection connection=null;
        try {
            connection = DBConnection.getInstance().getConnection();

            connection.setAutoCommit(false);//*************8

            String sql = "insert into orders Values(?,?,?)";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, orderDto.getOrderId());
            pstm.setString(2, orderDto.getDate());
            pstm.setString(3, orderDto.getCustomerId());
            if (pstm.executeUpdate() > 0) {
                boolean isSaved = orderDetailsModel.saveOrderDetails(orderDto.getList());
                if (isSaved) {
                    connection.commit();
                    return true;
                }
            }
        }catch (SQLException|ClassNotFoundException ex){
            connection.rollback();
            ex.printStackTrace();
            connection.setAutoCommit(true);
        }

        return false;
    }

    @Override
    public OrderDto lastOrder() throws SQLException, ClassNotFoundException {
        String sql="select * from orders ORDER BY OrderId DESC limit 1";

            Connection connection= DBConnection.getInstance().getConnection();
            PreparedStatement pstm=connection.prepareStatement(sql);
            ResultSet res=pstm.executeQuery();
            while (res.next()){
                return new OrderDto(
                        res.getString(1),
                        res.getString(2),
                        res.getString(3),
                        null
                );
            }

        return null;
    }

    @Override
    public List<OrderDto> allOrders() throws SQLException, ClassNotFoundException {
        String sql="select * from orders";
        List<OrderDto> list=new ArrayList<>();
        Connection connection= DBConnection.getInstance().getConnection();
        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet res=pstm.executeQuery();
        while (res.next()){
            list.add( new OrderDto(
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    null
            ));
        }
        return list;
    }


}
