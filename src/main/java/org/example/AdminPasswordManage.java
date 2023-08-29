package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Scanner;

public class AdminPasswordManage {
    private static final String DB_URL = "jdbc:sqlite:admin.db";

    void adminPasswordManageStart() {
        System.out.println("-----管理员密码管理系统-----");
        System.out.println("修改管理员密码");
        adminPasswordModify();
    }

    //管理员修改管理员密码
    public static void adminPasswordModify() {
        String adminname = "admin";
        System.out.println("");

        Scanner scanner = new Scanner(System.in);
        int attempts = 0;

        while (attempts < 3) {
            System.out.println("请输入管理员旧密码：");
            String oldPassword = scanner.nextLine();
            boolean valid = validAdminPassword(adminname, oldPassword);

            if (valid) {
                System.out.println("密码正确，请输入新的管理员密码：");
                String newPassword = scanner.nextLine();
                updateAdminPassword(adminname, newPassword);
                return;
            } else {
                attempts++;
                if(attempts != 3) {
                    System.out.println("密码错误，还有" + (3 - attempts) + "次机会！");
                }
            }
        }

        System.out.println("密码输入错误次数超过限制，修改密码失败！请联系相关人员解决！");
    }

    //管理员重置用户密码
    public void resetUserPassword(String username) {
        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%";
        StringBuilder randomId = new StringBuilder();
        while (randomId.length() < 8) {
            int index = (int) (Math.random() * characters.length());
            randomId.append(characters.charAt(index));
        }
        String newUserPassword = randomId.toString();

        System.out.println("**您有一封新的邮件！**");
        System.out.println("您的新密码为" +"“" + newUserPassword + "”," + "请使用该密码进行登录");
        updateUserPassword(username, newUserPassword);
    }

    //管理员密码验证
    private static boolean validAdminPassword(String adminname, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // 查询管理员信息
            String query = "SELECT * FROM admins WHERE adminname = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, adminname);
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

    //数据库更新管理员密码
    public static void updateAdminPassword(String adminname, String newPassword) {

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            // 检查管理员是否存在
            String checkSql = "SELECT adminname FROM admins WHERE adminname = '" + adminname + "'";
            ResultSet resultSet = statement.executeQuery(checkSql);

            if (resultSet.next()) {
                // 更新密码
                String updateSql = "UPDATE admins SET password = '" + newPassword +
                        "' WHERE adminname = '" + adminname + "'";
                int affectedRows = statement.executeUpdate(updateSql);

                if (affectedRows > 0) {
                    System.out.println("密码修改成功。");
                } else {
                    System.out.println("密码修改失败。");
                }
            } else {
                System.out.println("未找到该管理员账号。");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //管理员重置用户密码后，用户密码更新
    private static void updateUserPassword(String username, String newPassword) {
        String newHashPassword = hashPassword(newPassword);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:user.db")) {
            // 修改用户密码
            String updatePasswordQuery = "UPDATE users SET password = ? WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updatePasswordQuery)) {
                pstmt.setString(1, newHashPassword);
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

    public static void main(String[] args) {
        adminPasswordModify();
    }

}
