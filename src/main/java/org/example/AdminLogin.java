package org.example;

import java.sql.*;
import java.util.Scanner;

public class AdminLogin {
    private static final String DB_URL = "jdbc:sqlite:admin.db";
    private static AfterAdminLogin afterAdminLogin = new AfterAdminLogin();
    private static Scanner scanner = new Scanner(System.in);

    public void adminLoginStart() {

        try {
            Class.forName("org.sqlite.JDBC"); // 加载SQLite驱动程序
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("请输入管理员密码：");
        String password = scanner.nextLine();

        boolean loginSuccess = login("admin", password);

        if (loginSuccess) {
            System.out.println("管理员登录成功！");

            // 进行后续管理员操作
            afterAdminLogin.afterAdminLogin();

        } else {
            System.out.println("管理员登录失败，请确认后重试！");
            // 管理员登录失败后续处理
        }
    }

    private static boolean login(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // 创建数据库表（如果不存在）
            stmt.execute("CREATE TABLE IF NOT EXISTS admins (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255))");

            // 查询管理员信息
            String query = "SELECT * FROM admins WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");

                // 检查密码是否匹配
                return password.equals(storedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    //初始化管理员账号
    private static void initial(String adminname, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
         Statement stmt = conn.createStatement()) {
        // 创建数据库表（如果不存在）
        stmt.execute("CREATE TABLE IF NOT EXISTS admins (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255))");

        String insertQuery = "INSERT INTO admins (username, password) VALUES (?, ?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
            insertStatement.setString(1, adminname);
            insertStatement.setString(2, password);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //测试 初始化
    public static void main(String[] args) {
        //initial("admin", "ynuinfo#777");
    }

}
