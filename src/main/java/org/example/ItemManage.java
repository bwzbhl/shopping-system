package org.example;

import java.sql.*;
import java.util.*;

public class ItemManage {

    static Scanner scanner = new Scanner(System.in);

    //商品管理入口
    public static void itemManageStart() {

        System.out.println("-----商品管理-----");
        while (true) {
            System.out.println("1. 列出所有商品信息");
            System.out.println("2. 添加商品信息");
            System.out.println("3. 修改商品信息");
            System.out.println("4. 删除商品信息");
            System.out.println("5. 查询商品信息");
            System.out.println("6. 退出");
            System.out.println("请选择功能：");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 清除输入的换行符

            switch (choice) {
                case 1:
                    listAllItems();
                    break;
                case 2:
                    addItem();
                    break;
                case 3:
                    modifyItemInfo();
                    break;
                case 4:
                    deleteItem();
                    break;
                case 5:
                    queryItemInfo();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }

        }
    }

    //添加商品
    private static void addItem() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:item.db")) {
            // 创建item表
            String createTableQuery = "CREATE TABLE IF NOT EXISTS item (" +
                    "item_id INTEGER PRIMARY KEY," +
                    "item_name TEXT," +
                    "manufacturer TEXT," +
                    "production_date TEXT," +
                    "model TEXT," +
                    "purchase_price REAL," +
                    "retail_price REAL," +
                    "quantity INTEGER" +
                    ")";
            conn.createStatement().execute(createTableQuery);

            // 事先加入商品信息

            ItemInfo item1 = new ItemInfo(1, "商品1", "制造商1", "2023-07-01", "型号1", 10.5, 19.9, 100);
            insertItemInfo(conn, item1);

            ItemInfo item2 = new ItemInfo(2, "商品2", "制造商2", "2023-07-02", "型号2", 15.5, 29.9, 200);
            insertItemInfo(conn, item2);

            ItemInfo item3 = new ItemInfo(3, "商品3", "制造商3", "2023-07-03", "型号3", 20.5, 39.9, 150);
            insertItemInfo(conn, item3);

            ItemInfo item4 = new ItemInfo(4, "商品4", "制造商4", "2023-07-04", "型号4", 25.5, 49.9, 300);
            insertItemInfo(conn, item4);

            ItemInfo item5 = new ItemInfo(5, "商品5", "制造商5", "2023-07-05", "型号5", 30.5, 59.9, 250);
            insertItemInfo(conn, item5);


            System.out.println("请输入商品ID: ");
            int itemId = scanner.nextInt();
            scanner.nextLine();

            System.out.println("请输入商品名称: ");
            String itemName = scanner.nextLine();

            System.out.println("请输入制造商: ");
            String manufacturer = scanner.nextLine();

            System.out.println("请输入生产日期: ");
            String productionDate = scanner.nextLine();

            System.out.println("请输入型号: ");
            String model = scanner.nextLine();

            System.out.println("请输入进货价格: ");
            double purchasePrice = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("请输入零售价格: ");
            double retailPrice = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("请输入数量: ");
            int quantity;
            try {
                quantity = scanner.nextInt();
                // 处理商品数量的逻辑
            } catch (InputMismatchException e) {
                System.out.println("无效的输入，请输入整数值。");
                scanner.nextLine(); // 清除输入的非整数值
                quantity = scanner.nextInt();
            }
            scanner.nextLine();

            ItemInfo item = new ItemInfo(itemId, itemName, manufacturer, productionDate, model, purchasePrice, retailPrice, quantity);
            insertItemInfo(conn, item);

            System.out.println("商品信息添加成功！");
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }

    }

    //商品信息插入数据库
    private static void insertItemInfo(Connection connection, ItemInfo item) throws SQLException {
        String insertQuery = "INSERT INTO item (item_id, item_name, manufacturer, production_date, model, purchase_price, retail_price, quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setInt(1, item.getItemId());
        preparedStatement.setString(2, item.getItemName());
        preparedStatement.setString(3, item.getManufacturer());
        preparedStatement.setString(4, item.getProductionDate());
        preparedStatement.setString(5, item.getModel());
        preparedStatement.setDouble(6, item.getPurchasePrice());
        preparedStatement.setDouble(7, item.getRetailPrice());
        preparedStatement.setInt(8, item.getQuantity());
        preparedStatement.executeUpdate();
    }

    // 修改商品信息
    public static void modifyItemInfo() {
        System.out.println("请给出所要修改的商品ID：");
        int itemId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("请输入以下更新后的相关商品信息：");
        System.out.println("商品名称：");
        String newName = scanner.nextLine();
        System.out.println("生产厂家：");
        String newManufacture = scanner.nextLine();
        System.out.println("进货价格：");
        String newPurchasePrice = scanner.nextLine();
        System.out.println("零售价格：");
        String newRetailPrice = scanner.nextLine();
        System.out.println("库存数量：");
        int newQuantity;
        try {
            newQuantity = scanner.nextInt();
            // 处理商品数量的逻辑
        } catch (InputMismatchException e) {
            System.out.println("无效的输入，请输入整数值。");
            scanner.nextLine(); // 清除输入的非整数值
            newQuantity = scanner.nextInt();
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:item.db")) {
            String updateQuery = "UPDATE item SET item_name = ?, manufacturer = ?, " +
                    "purchase_price = ?, retail_price = ?, quantity = ? WHERE item_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);

            pstmt.setString(1, newName);
            pstmt.setString(2, newManufacture);
            pstmt.setString(3, newPurchasePrice);
            pstmt.setString(4, newRetailPrice);
            pstmt.setInt(5, newQuantity);
            pstmt.setInt(6, itemId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("商品信息修改成功！");
            } else {
                System.out.println("找不到指定商品！");
            }

        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }

    }

    //删除商品
    private static void deleteItem() {
        System.out.println("请输入所要删除的商品的编号:");
        int itemId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:item.db")) {
            String deleteQuery = "DELETE FROM item WHERE item_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, itemId);
            // 提示用户确认删除操作
            System.out.println("删除商品信息是不可撤销的操作！");
            System.out.println("确认是否继续删除操作？（Y/N）");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("商品信息删除成功！");
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

    //查询商品
    private static void queryItemInfo() {
        System.out.println("可根据商品名称、生产厂家、零售价格进行单独查询或者组合查询");
        System.out.println("请输入商品名称（留空表示不使用该条件）: ");
        String itemName = scanner.nextLine();

        System.out.println("请输入生产厂家（留空表示不使用该条件）: ");
        String manufacturer = scanner.nextLine();

        System.out.println("请输入零售价格（输入负数表示不使用该条件）（返回大于等于该价格的）: ");
        double retailPrice = scanner.nextDouble();
        scanner.nextLine();

        List<ItemInfo> searchResult = searchItemInfo(itemName, manufacturer, retailPrice);

        if (searchResult.isEmpty()) {
            System.out.println("没有找到符合条件的商品信息。");
        } else {
            System.out.println("符合查询条件的商品信息如下：");
            System.out.println("-------------");
            for (ItemInfo item : searchResult) {
                System.out.println("商品编号：" + item.getItemId());
                System.out.println("商品名称：" + item.getItemName());
                System.out.println("生产厂家：" + item.getManufacturer());
                System.out.println("商品型号：" + item.getModel());
                System.out.println("生产日期：" + item.getProductionDate());
                System.out.println("进货价：" + item.getPurchasePrice());
                System.out.println("零售价：" + item.getRetailPrice());
                System.out.println("现有库存量：" + item.getQuantity());
                System.out.println("-----------");
            }
        }

    }

    private static List<ItemInfo> searchItemInfo(String itemName, String manufacturer, double retailPrice) {
        List<ItemInfo> resultList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:item.db")) {
            // 构建查询语句
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM item WHERE 1=1");

            if (itemName != null && !itemName.isEmpty()) {
                queryBuilder.append(" AND item_name LIKE ?");
            }

            if (manufacturer != null && !manufacturer.isEmpty()) {
                queryBuilder.append(" AND manufacturer LIKE ?");
            }

            if (retailPrice >= 0) {
                queryBuilder.append(" AND retail_price >= ?");
            }

            // 创建 PreparedStatement 对象
            PreparedStatement preparedStatement = conn.prepareStatement(queryBuilder.toString());

            // 绑定参数值
            int parameterIndex = 1;
            if (itemName != null && !itemName.isEmpty()) {
                preparedStatement.setString(parameterIndex++, "%" + itemName + "%");
            }

            if (manufacturer != null && !manufacturer.isEmpty()) {
                preparedStatement.setString(parameterIndex++, "%" + manufacturer + "%");
            }

            if (retailPrice >= 0) {
                preparedStatement.setDouble(parameterIndex, retailPrice);
            }

            // 执行查询
            ResultSet resultSet = preparedStatement.executeQuery();

            // 解析结果集
            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                String itemNameResult = resultSet.getString("item_name");
                String manufacturerResult = resultSet.getString("manufacturer");
                String productionDate = resultSet.getString("production_date");
                String model = resultSet.getString("model");
                double purchasePrice = resultSet.getDouble("purchase_price");
                double retailPriceResult = resultSet.getDouble("retail_price");
                int quantity = resultSet.getInt("quantity");

                ItemInfo item = new ItemInfo(itemId, itemNameResult, manufacturerResult, productionDate, model, purchasePrice, retailPriceResult, quantity);
                resultList.add(item);
            }

        } catch(SQLException e){
            System.out.println("数据库连接错误：" + e.getMessage());
        }
        return resultList;
    }

    //列出所有商品信息
    static void listAllItems() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:item.db")) {
            String selectQuery = "SELECT * FROM item";
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("商品信息列表：");
            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                String itemName = resultSet.getString("item_name");
                String manufacturer = resultSet.getString("manufacturer");
                String productionDate = resultSet.getString("production_date");
                String model = resultSet.getString("model");
                double purchasePrice = resultSet.getDouble("purchase_price");
                double retailPrice = resultSet.getDouble("retail_price");
                int quantity = resultSet.getInt("quantity");

                System.out.println("商品编号：" + itemId);
                System.out.println("商品名称：" + itemName);
                System.out.println("生产厂家：" + manufacturer);
                System.out.println("生产日期：" + productionDate);
                System.out.println("型号：" + model);
                System.out.println("进货价：" + purchasePrice);
                System.out.println("零售价格：" + retailPrice);
                System.out.println("现有库存数量：" + quantity);
                System.out.println("------------------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    //测试
    public static void main(String[] args) {
        addItem();//初始化商品信息
    }
}
