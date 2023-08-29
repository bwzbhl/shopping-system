package org.example;

import java.sql.*;
import java.util.Scanner;

public class CustomerManage {

    static Scanner scanner = new Scanner(System.in);

    public void customerManageStart() {
        System.out.println("-----客户管理系统-----");
        while (true) {
            System.out.println("1. 列出所有客户信息");
            System.out.println("2. 删除客户信息");
            System.out.println("3. 查询客户信息");
            System.out.println("4. 退出");
            System.out.println("请选择功能：");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 清除输入的换行符

            switch (choice) {
                case 1:
                    listAllCustomer();
                    break;
                case 2:
                    delCustomer();
                    break;
                case 3:
                    queryCustomer();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }

        }
    }

    private static void listAllCustomer() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:customer.db")) {
            String selectQuery = "SELECT * FROM customer";
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("客户信息列表：");
            System.out.println("-------------");
            while (resultSet.next()) {
                String cusName = resultSet.getString("customerName");
                String cusId = resultSet.getString("customerId");
                String cusLevel = resultSet.getString("customerLevel");
                String regDate = resultSet.getString("registrationDate");
                double purSum = resultSet.getDouble("totalPurchaseAmount");
                String phone = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");

                System.out.println("客户ID：" + cusId);
                System.out.println("客户名称：" + cusName);
                System.out.println("客户等级：" + cusLevel);
                System.out.println("注册日期：" + regDate);
                System.out.println("购买总金额：" + purSum);
                System.out.println("电话号码：" + phone);
                System.out.println("邮箱：" + email);
                System.out.println("----------------------");
            }

        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    private static void delCustomer() {

        System.out.println("请输入所要删除客户的客户ID:");
        String cusId = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:customer.db")) {
            String deleteQuery = "DELETE FROM customer WHERE customerId = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setString(1, cusId);
            // 提示用户确认删除操作
            System.out.println("删除客户信息是不可撤销的操作！");
            System.out.println("确认是否继续删除操作？（Y/N）");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("客户信息删除成功！");
                } else {
                    System.out.println("找不到指定客户！");
                }
            } else {
                System.out.println("已取消删除操作！");
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }

    }

    private static void queryCustomer() {

        System.out.println("1. 根据客户ID查询");
        System.out.println("2. 根据客户用户名查询");
        System.out.println("3. 查询所有客户");
        System.out.println("请选择查询方式");
        int choice = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:customer.db")) {
            if(choice == 1) {
                System.out.println("请输入所要查询的客户ID：");
                String cusId = scanner.nextLine();

                String query = "SELECT * FROM customer WHERE customerId = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, cusId);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("客户ID：" + resultSet.getString("customerId"));
                    System.out.println("客户名称：" + resultSet.getString("customerName"));
                    System.out.println("客户等级：" + resultSet.getString("customerLevel"));
                    System.out.println("注册日期：" + resultSet.getString("registrationDate"));
                    System.out.println("购买总金额：" + resultSet.getDouble("totalPurchaseAmount"));
                    System.out.println("电话号码：" + resultSet.getString("phoneNumber"));
                    System.out.println("邮箱：" + resultSet.getString("email"));
                }
                else System.out.println("未查询到相关客户！");
            } else if (choice == 2) {
                System.out.println("请输入所要查询的用户名：");
                String cusName = scanner.nextLine();

                String query = "SELECT * FROM customer WHERE customerName = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, cusName);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("客户ID：" + resultSet.getString("customerId"));
                    System.out.println("客户名称：" + resultSet.getString("customerName"));
                    System.out.println("客户等级：" + resultSet.getString("customerLevel"));
                    System.out.println("注册日期：" + resultSet.getString("registrationDate"));
                    System.out.println("购买总金额：" + resultSet.getDouble("totalPurchaseAmount"));
                    System.out.println("电话号码：" + resultSet.getString("phoneNumber"));
                    System.out.println("邮箱：" + resultSet.getString("email"));
                }
                else System.out.println("未查询到相关客户！");
            } else listAllCustomer();

        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        //customerManageStart();
    }
}


