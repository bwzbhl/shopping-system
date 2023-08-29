package org.example;

import java.security.*;
import java.sql.*;
import java.util.Scanner;

public class UserRegistration {
    static Scanner scanner = new Scanner(System.in);
    static AddCustomerInfo addCustomerInfo = new AddCustomerInfo();
    // 加密密码
    private static String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 检查密码是否满足要求
    private static boolean isValidPassword(String password) {
        if (password.length() <= 8) {
            return false;
        }
        // 判断密码是否包含大小写字母、数字、标点符号
        if (!password.matches(".*[a-z].*") ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*\\d.*") ||
                !password.matches(".*[!@#$%^&*()].*")) {
            return false;
        }
        return true;
    }

    public static String getAvailUsername() {
        String username = "";

        while (true) {
            // (重新)输入用户名
            System.out.print("请输入用户名(不少于5个字符)：");
            username = scanner.nextLine();

            if (username.length() < 5) {
                System.out.println("用户名不符合要求，请重新输入");
                continue;
            }

            // 连接到 SQLite 数据库
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:user.db")) {
                // 创建用户表（如果不存在）
                String createTableQuery = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, locked INTEGER DEFAULT 0)";
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createTableQuery);
                }

                // 检查用户名是否已经存在
                String checkUserExistsQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(checkUserExistsQuery)) {
                    pstmt.setString(1, username);
                    ResultSet rs = pstmt.executeQuery();
                    int count = rs.getInt(1);
                    if (count > 0) {
                        System.out.println("用户名已存在，请重新输入");
                        continue;
                    }
                }

            } catch (SQLException e) {
                System.out.println("数据库连接错误：" + e.getMessage());
            }
            return username;
        }
    }

    //获得可行密码 （返回的是加密过后的密码
    public static String getAvailPassword() {
        String password = "";

        while (true) {
            // (重新)输入密码
            System.out.print("请输入密码(不少于9位，必须为大小写字母、数字、标点符号的组合)：");
            password = scanner.nextLine();

            // 检查密码是否符合要求
            if (!isValidPassword(password)) {
                System.out.println("密码不符合要求，请重新输入");
                continue;
            }

            // 加密密码
            String encryptedPassword = encryptPassword(password);
            return encryptedPassword;
        }
    }

    //用户注册入口
    public void registrationStart() {
        String username = "";
        String password = "";

        username = getAvailUsername();
        password = getAvailPassword();

        // 连接到 SQLite 数据库
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:user.db")) {
            // 创建用户表（如果不存在）
            String createTableQuery = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, locked INTEGER DEFAULT 0)";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableQuery);
            }

            // 插入新用户
            String insertUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertUserQuery)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
            }

            System.out.println("用户注册成功！");

            //注册成功后完善客户信息
            System.out.println("请输入您的电话号码：");
            String phone = scanner.nextLine();
            System.out.println("请输入您的邮箱：");
            String email = scanner.nextLine();
            addCustomerInfo.addCustomer(username, phone, email);
            System.out.println("基本信息完成！恭喜您成为一名顾客！");
            home();

        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    public static void home() {
        UserSystem userSystem = new UserSystem();
        AdminSystem adminSystem = new AdminSystem();

        System.out.println("------欢迎来到购物系统------");
        System.out.println("请选择身份进入购物系统：");
        System.out.println("1. 管理员");
        System.out.println("2. 用户");
        System.out.println("3. 退出购物系统");
        int choice = scanner.nextInt();

        if (choice == 1)
            adminSystem.adminSystemStart();
        else if(choice == 2) userSystem.userSystemStart();
        else System.exit(0);
    }

    //测试
    public static void main(String[] args) {
        //registrationStart();
    }
}