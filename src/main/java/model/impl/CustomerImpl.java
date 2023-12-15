package model.impl;

import db.DBConnection;
import dto.CustomerDto;
import dto.tm.CustomerTm;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import model.CustomerModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerImpl implements CustomerModel {
    @Override
    public boolean saveCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {

        String sql = "insert into customer values('" + dto.getId() + "','" + dto.getName() + "','" + dto.getAddress()+ "'," + dto.getSalary() + ")";

            Connection conn = DBConnection.getInstance().getConnection();
            Statement stm = conn.createStatement();
            int res = stm.executeUpdate(sql);
            return res>0 ? true :false;
    }
    @Override
    public boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        String sql = "update Customer set custName='" + dto.getName() + "'," + "custAddress='" + dto.getAddress() + "'," + "Salary=" + dto.getSalary() + " where custid='" + dto.getId() + "'";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        int res = stm.executeUpdate(sql);
        if (res>0)return true;
        return false;
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        String sql = "delete from customer where custId='" + id + "'";
        Connection conn =DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        int res = stm.executeUpdate(sql);
        if (res>0){
            return true;
        }
        return false;
    }

    @Override
    public List<CustomerDto> allCustomer() throws SQLException, ClassNotFoundException {

    List<CustomerDto> list=new ArrayList<>();
        String sql = "select * from customer";
        Connection conn= DBConnection.getInstance().getConnection();
        PreparedStatement pstm= conn.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        while (rst.next()){
            list.add(new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4)
            ));

        }
        return list;
    }
}