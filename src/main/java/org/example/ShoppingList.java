package org.example;

import java.sql.*;

public class ShoppingList {
    private String purchaseDate;
    private int purchaseItemId;
    private int purchaseItemQuantity;
    private double purchaseSum;

    public ShoppingList() {}

    public ShoppingList(String purchaseDate, int purchaseItemId, int purchaseItemQuantity, double purchaseSum) {
        this.purchaseDate = purchaseDate;
        this.purchaseItemId = purchaseItemId;
        this.purchaseItemQuantity = purchaseItemQuantity;
        this.purchaseSum = purchaseSum;
    }

    public void setShopDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String purchaseDate() {
        return purchaseDate;
    }

    public void purchaseItemName(int purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }

    public int getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemQuantity(int purchaseItemQuantity) {
        this.purchaseItemQuantity = purchaseItemQuantity;
    }

    public int getPurchaseItemQuantity() {
        return purchaseItemQuantity;
    }

    public void setPurchaseSum(double purchaseSum) {
        this.purchaseSum = purchaseSum;
    }

    public double getPurchaseSum() {
        return purchaseSum;
    }

    //添加购物清单  购物历史
    static void addShopList(String date, int itemId, int quantity, double purchaseSum, String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:history.db")) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS history (" +
                    "date TEXT PRIMARY KEY," +
                    "item_id INTEGER," +
                    "item_quantity INTEGER,"  +
                    "purchase_sum DOUBLE," +
                    "username TEXT" +
                    ")";
            conn.createStatement().execute(createTableQuery);

            String insertQuery = "INSERT INTO history (date, item_id, item_quantity, purchase_sum, username) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, date);
            preparedStatement.setInt(2, itemId);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setDouble(4, purchaseSum);
            preparedStatement.setString(5, username);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }

    }

    //列出购物历史
    static void listShopHistory(String username) {

        System.out.println("您的购物历史如下：");
        System.out.println("--------------------");

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:history.db")) {
            String selectQuery = "SELECT * FROM history WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                String date = resultSet.getString("date");
                double purchaseSum = resultSet.getDouble("purchase_sum");
                int quantity = resultSet.getInt("item_quantity");

                System.out.println("购买时间：" + date);
                System.out.println("商品编号：" + itemId);
                System.out.println("购买数量：" + quantity);
                System.out.println("共计金额：" + purchaseSum);
                System.out.println("-----------------------");
            }

        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }
}
