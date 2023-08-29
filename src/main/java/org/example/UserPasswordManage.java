package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Scanner;

public class UserPasswordManage {

    static Scanner scanner = new Scanner(System.in);
    static AdminPasswordManage adminPasswordManage = new AdminPasswordManage();

    //用户密码管理入口  **用户名
    public static void userPassowrdManageStart(String username) {

        System.out.println("----进入“用户密码管理”------");

        System.out.println("-----修改密码-----");
        userPasswordModify(username);

    }

    //用户修改密码 **用户名*****
    public static void userPasswordModify(String username) {
        int attempts = 0;

        while (attempts < 3) {

            System.out.println("请输入您的旧密码：");
            String oldPassword = scanner.nextLine();
            boolean valid = verifyUserPassword(username, oldPassword);

            if (valid) {
                String newHashedPassword = getAvailPassword();;
                updateUserPassword(username, newHashedPassword);
                return;
            } else {

                attempts++;
                if(attempts != 3) {
                    System.out.println("密码错误，还有" + (3 - attempts) + "次机会！");
                }
            }
        }

        System.out.println("密码输入错误次数超过限制，修改密码失败！请过段时间再来！");
    }

    //用户忘记密码---管理员收到请求进行重置  用户名**
    public void userPasswordForget(String username) {
        System.out.println("请输入您的邮箱号");
        String email = scanner.nextLine();
        System.out.println("若三分钟内未收到邮件，请检查邮箱是否正确");
        if(isEmail(username, email)) {
            adminPasswordManage.resetUserPassword(username);
        }else{
            System.out.println("youxiangcuowu");
        }
    }

    // 检查邮箱是否正确
    private static boolean isEmail(String username, String email) {
        boolean re = false;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:customer.db")) {

            String query = "SELECT * FROM customer WHERE customerName = ? AND email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);

            // 执行查询
            ResultSet resultSet = preparedStatement.executeQuery();

            // 检查是否有匹配的记录
            if (resultSet.next()) {
                re = true;
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
        return re;
    }

    //对输入的用户旧密码验证
    public static boolean verifyUserPassword(String username, String password) {
        // 对输入的密码进行 MD5 加密
        String hashedPassword = hashPassword(password);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:user.db")) {
            String verifyPasswordQuery = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(verifyPasswordQuery)) {
                pstmt.setString(1, username);
                pstmt.setString(2, hashedPassword);
                ResultSet rs = pstmt.executeQuery();
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }

        return false; // 若发生异常或查询结果为空，则返回 false
    }

    //用户密码数据库更新
    private static void updateUserPassword(String username, String newHashedPassword) {

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:user.db")) {
            // 修改用户密码
            String updatePasswordQuery = "UPDATE users SET password = ? WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updatePasswordQuery)) {
                pstmt.setString(1, newHashedPassword);
                pstmt.setString(2, username);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("密码修改成功！");
                } else {
                    System.out.println("没有找到匹配的用户。");
                }
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    //密码MD5加密
    private static String hashPassword(String password) {
        try {
            // 创建 MD5 摘要对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将密码转换为字节数组并进行加密
            byte[] passwordBytes = password.getBytes();
            byte[] hashedBytes = md.digest(passwordBytes);

            // 将加密后的字节数组转换为十六进制字符串表示
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 处理算法不支持的异常情况
            e.printStackTrace();
        }
        return null; // 若发生异常，则返回 null
    }

    public static String getAvailPassword() {
        String password;

        while (true) {
            // (重新)输入密码
            System.out.println("请输入新密码(不少于9位，必须为大小写字母、数字、标点符号的组合)：");
            password = scanner.nextLine();

            // 检查密码是否符合要求
            if (!isValidPassword(password)) {
                System.out.println("密码不符合要求，请重新输入");
                continue;
            }

            // 加密密码
            String encryptedPassword = hashPassword(password);
            return encryptedPassword;
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

    //测试
    public static void main(String[] args) {
    }
}
