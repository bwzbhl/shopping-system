package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class AddCustomerInfo {

    // 添加客户信息到数据库
    public void addCustomer(String customerName, String phoneNumber, String email) {
        // 生成客户ID
        String customerId = generateRandomId();

        // 获取当前时间作为注册日期
        String registrationDate = getCurrentDateTime();

        // 初始化累计购买金额为0
        double totalPurchaseAmount = 0;

        // 默认客户初始级别为铜牌客户
        String customerLevel = "铜牌客户";

        // 创建连接和语句对象
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:customer.db");
             Statement statement = connection.createStatement()) {
            // 创建客户信息表
            String createTableSql = "CREATE TABLE IF NOT EXISTS customer ("
                    + "customerId TEXT NOT NULL, "
                    + "customerName TEXT NOT NULL, "
                    + "customerLevel TEXT NOT NULL, "
                    + "registrationDate TEXT NOT NULL, "
                    + "totalPurchaseAmount REAL NOT NULL, "
                    + "phoneNumber TEXT, "
                    + "email TEXT)";
            statement.execute(createTableSql);

            // 插入客户信息
            String insertSql = "INSERT INTO customer (customerId, customerName, customerLevel, registrationDate, totalPurchaseAmount, phoneNumber, email) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, customerId);
            preparedStatement.setString(2, customerName);
            preparedStatement.setString(3, customerLevel);
            preparedStatement.setString(4, registrationDate);
            preparedStatement.setDouble(5, totalPurchaseAmount);
            preparedStatement.setString(6, phoneNumber);
            preparedStatement.setString(7, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 获取当前日期和时间
    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.toString();
    }

    // 生成随机客户ID
    private String generateRandomId() {
        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()-_=+[{]};:',<.>/?";
        StringBuilder randomId = new StringBuilder();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:customer.db");
             Statement statement = connection.createStatement()) {

            String createTableSql = "CREATE TABLE IF NOT EXISTS customer ("
                    + "customerId TEXT NOT NULL, "
                    + "customerName TEXT NOT NULL, "
                    + "customerLevel TEXT NOT NULL, "
                    + "registrationDate TEXT NOT NULL, "
                    + "totalPurchaseAmount REAL NOT NULL, "
                    + "phoneNumber TEXT, "
                    + "email TEXT)";
            statement.execute(createTableSql);

            // 查询已存在的客户ID
            String querySql = "SELECT customerId FROM customer";
            ResultSet resultSet = statement.executeQuery(querySql);

            Set<String> existingIds = new HashSet<>();
            while (resultSet.next()) {
                int customerId = resultSet.getInt("customerId");
                existingIds.add(Integer.toString(customerId));
            }

            // 生成唯一的8位随机ID
            while (randomId.length() < 8) {
                int index = (int) (Math.random() * characters.length());
                randomId.append(characters.charAt(index));
            }

            // 检查是否存在重复的ID，若有重复则重新生成
            while (existingIds.contains(randomId.toString())) {
                randomId.setLength(0); // 清空字符串
                while (randomId.length() < 8) {
                    int index = (int) (Math.random() * characters.length());
                    randomId.append(characters.charAt(index));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return randomId.toString();
    }
}