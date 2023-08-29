package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ShoppingCart {
    static ItemManage itemManage = new ItemManage();
    static ShoppingList shoppingList = new ShoppingList();
    static Scanner scanner = new Scanner(System.in);

    // 购物入口 **用户名
    static void shoppingStart(String customerName) {

        System.out.println("----进入“购物”------");
        System.out.println("*****商品浏览*****");
        displayItem();

        while (true) {

            System.out.println("请选择：");
            System.out.println("1. 添加商品到购物车");
            System.out.println("2. 修改购物车中的某商品的购买数量");
            System.out.println("3. 删除购物车中的某商品");
            System.out.println("4. 购物车中选择商品结算");
            System.out.println("5. 查看购物历史");
            System.out.println("6. 查看我的购物车");
            System.out.println("7. 退出");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addItemToCart(customerName);
                    break;
                case 2:
                    modifyItemToCart(customerName);
                    break;
                case 3:
                    delItemInCart(customerName);
                    break;
                case 4:
                    payment(customerName);
                    break;
                case 5:
                    shoppingList.listShopHistory(customerName);
                    break;
                case 6:
                    displayCart(customerName);
                    break;
                case 7:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
                    break;
            }

        }
    }

    // 浏览商品
    private static void displayItem() {
        itemManage.listAllItems();
    }

    private static void displayCart(String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:cart.db")) {
            String selectQuery = "SELECT * FROM cart WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            // if (resultSet.next()) {

            System.out.println("当前购物车已有商品如下：");
            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                int itemQuantity = resultSet.getInt("item_quantity");

                System.out.println("商品编号：" + itemId);
                System.out.println("欲购买数量：" + itemQuantity);
                System.out.println("-----------------------");
            }
            // }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    private static void addItemToCart(String username) {
        System.out.println("请输入以下信息将商品加入购物车");
        System.out.println("商品编号：");
        int itemId = scanner.nextInt();
        System.out.println("购买数量：");
        int itemQuantity = scanner.nextInt();

        // 创建购物车数据库连接
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:cart.db")) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS cart (" +
                    "item_id INTEGER," +
                    "item_quantity INTEGER," +
                    "username TEXT" +
                    ")";
            conn.createStatement().execute(createTableQuery);

            if (!isItemInCart(itemId, username)) {
                String insertQuery = "INSERT INTO cart (item_id, item_quantity, username) " + "VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
                preparedStatement.setInt(1, itemId);
                preparedStatement.setInt(2, itemQuantity);
                preparedStatement.setString(3, username);
                preparedStatement.executeUpdate();

                System.out.println("添加购物车成功！");
            } else {
                System.out.println("当前购物车已存在该商品，请前去“修改商品数量”");
                modifyItemToCart(username);
            }

        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    private static void delItemInCart(String username) {
        System.out.println("请输入所要删除的商品的编号:");
        int itemId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:cart.db")) {
            String deleteQuery = "DELETE FROM cart WHERE item_id = ? AND username = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, itemId);
            pstmt.setString(2, username);
            // 提示用户确认删除操作
            System.out.println("确认是否将该商品从购物车里删除？（Y/N）");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("删除成功！");
                } else {
                    System.out.println("找不到指定商品！");
                }
            } else {
                System.out.println("已取消删除操作！");
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    // 支付
    static void payment(String cusName) {
        System.out.println("请输入要支付的商品ID");
        int itemId = scanner.nextInt();
        int quantity;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:cart.db")) {
            String query = "SELECT * FROM cart WHERE item_id = ? AND username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, itemId);
            preparedStatement.setString(2, cusName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                query = "SELECT * FROM cart WHERE item_id = ? AND username = ?";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, itemId);
                preparedStatement.setString(2, cusName);

                resultSet = preparedStatement.executeQuery();
                quantity = resultSet.getInt("item_quantity"); // 获得购买数量

                double shoppingSum = total(itemId, quantity);
                double newPurchaseAmount = updateCustomerCost(cusName, shoppingSum);
                updateCustomerLevel(cusName, newPurchaseAmount);
                inventoryModify(itemId, quantity);
                String date = getCurrentDateTime();
                shoppingList.addShopList(date, itemId, quantity, shoppingSum, cusName);

                preparedStatement.close();
                String deleteQuery = "DELETE FROM cart WHERE item_id = ? AND username = ?";
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                pstmt.setInt(1, itemId);
                pstmt.setString(2, cusName);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("支付完成，商品已从购物车中删除！");
                }

            } else {
                System.out.println("找不到指定商品！");
            }

        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    // 修改购物车中的商品数量
    private static void modifyItemToCart(String username) {

        System.out.println("请输入要修改数量的商品ID");
        int itemId = scanner.nextInt();
        System.out.println("请输入修改后的购买数量：(输入数量非正将默认移除该商品）");
        int newQuantity = scanner.nextInt();

        if (newQuantity <= 0)
            delItemInCart(username);
        else {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:cart.db")) {
                String updateQuery = "UPDATE cart SET item_quantity = ? WHERE item_id = ? AND username = ?";
                PreparedStatement pstmt = conn.prepareStatement(updateQuery);

                pstmt.setInt(1, newQuantity);
                pstmt.setInt(2, itemId);
                pstmt.setString(3, username);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("购物车修改商品数量成功！");
                } else
                    System.out.println("购物车中不存在该商品！请查看购物车");

            } catch (SQLException e) {
                System.out.println("数据库连接错误：" + e.getMessage());
            }
        }
    }

    // 付账后修改商品库存
    static void inventoryModify(int itemId, int quantityDel) {

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:item.db")) {

            String query = "SELECT * FROM item WHERE item_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, itemId);

            ResultSet resultSet = preparedStatement.executeQuery();
            int initInventory = resultSet.getInt("quantity");

            if (resultSet.next()) {
                String updateQuery = "UPDATE item SET quantity = ? WHERE item_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(updateQuery);

                int newInventory = initInventory - quantityDel;
                pstmt.setInt(1, newInventory);
                pstmt.setInt(2, itemId);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("商品库存数量更新成功！");
                }
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    // 检查购物车中是否存在某商品
    public static boolean isItemInCart(int itemId, String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:cart.db")) {
            String query = "SELECT COUNT(*) FROM cart WHERE item_id = ? AND username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, itemId);
            statement.setString(2, username);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int count = result.getInt(1);
                return count > 0; // 如果 count 大于 0，表示记录存在于购物车中
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 计算购物付账所需金额
    public static double total(int itemId, int quantity) {

        double retailPrice = 0;
        // 根据商品编号查询售价
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:item.db")) {
            String query = "SELECT * FROM item WHERE item_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, itemId);

            // 执行查询
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                retailPrice = quantity * resultSet.getDouble("retail_price");
            } else {
                System.out.println("商品编号错误！不存在该编号对应商品");
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
        return retailPrice * quantity;
    }

    // 更新客户的消费总金额 **用户名
    public static double updateCustomerCost(String cusName, double cost) {

        double newTotalCost = 0.0;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:customer.db")) {
            String query = "SELECT * FROM customer WHERE customerName = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, cusName);

            ResultSet resultSet = preparedStatement.executeQuery();
            double initialCost = resultSet.getDouble("totalPurchaseAmount");
            if (resultSet.next()) {
                String updateQuery = "UPDATE customer SET totalPurchaseAmount = ? WHERE customerName = ?";
                PreparedStatement pstmt = conn.prepareStatement(updateQuery);

                newTotalCost = cost + initialCost;
                pstmt.setDouble(1, newTotalCost);
                pstmt.setString(2, cusName);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("客户消费总金额更新成功！");
                }
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }

        return newTotalCost;
    }

    // 更新客户消费等级 **用户名
    static void updateCustomerLevel(String cusName, double curCost) {

        String newLevel = getlevel(curCost);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:customer.db")) {
            String query = "SELECT * FROM customer WHERE customerName = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, cusName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String updateQuery = "UPDATE customer SET customerLevel = ? WHERE customerName = ?";
                PreparedStatement pstmt = conn.prepareStatement(updateQuery);

                pstmt.setString(1, newLevel);
                pstmt.setString(2, cusName);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("客户等级更新成功！");
                }
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    // 判断消费等级
    static String getlevel(double totalCost) {
        if (totalCost <= 300)
            return "铜牌客户";
        else if (totalCost > 300 && totalCost <= 600) {
            return "银牌客户";
        } else
            return "金牌客户";
    }

    // 创建购物历史--获取时间
    private static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.toString();
    }

    // 测试
    public static void main(String[] args) {

    }

}
