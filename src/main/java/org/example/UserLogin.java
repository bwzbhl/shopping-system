package org.example;

import java.sql.*;
import java.util.Scanner;
import java.security.*;

public class UserLogin {
    static Scanner scanner = new Scanner(System.in);
    static  AfterUserLogin afterUserLogin = new AfterUserLogin();
    static UserPasswordManage userPasswordManage = new UserPasswordManage();
   //用户登录入口
   public void userLoginStart() {
       String username = "";
       String password = "";

       System.out.print("请输入用户名：");
       username = scanner.nextLine();

       if(!isUser(username)) {
           System.out.println("该用户不存在，请确定后重试！");
       }
       else {
           System.out.println("1. 密码登录");
           System.out.println("2. 忘记密码");
           System.out.println("请选择：");
           int choice = scanner.nextInt();
           scanner.nextLine();

           if (choice == 1) {
               // 连续输入错误次数
               int failedAttempts = 0;

               if (isLocked(username)) {
                   System.out.println("账户已被锁定，请联系管理员解锁！");
                   return;
               }

               while (failedAttempts < 5) {
                   System.out.println("请输入密码：");
                   password = scanner.nextLine();

                   if (verifyPassword(username, password)) {
                       System.out.println("登录成功！");

                       //登录成功后
                       afterUserLogin.afterUserLogin(username);
                       //return;
                   } else {
                       failedAttempts++;
                       System.out.println("密码错误！还剩下 " + (5 - failedAttempts) + " 次尝试机会。");

                       if (failedAttempts == 5) {
                           lockAccount(username);
                           return;
                       }
                   }
               }
           } else {
               userPasswordManage.userPasswordForget(username);

               System.out.print("请输入重置后的新密码：");
               String newPassword = scanner.nextLine();

               if (verifyPassword(username, newPassword)) {
                   System.out.println("登录成功！");

                   //登录成功后
                   afterUserLogin.afterUserLogin(username);
               }
           }

       }
   }

    private static boolean isLocked(String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:user.db")) {
            String checkLockQuery = "SELECT COUNT(*) FROM users WHERE username = ? AND locked = 1";
            try (PreparedStatement pstmt = conn.prepareStatement(checkLockQuery)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
        return false;
    }

    //用户名是否存在
    private static boolean isUser(String username) {
        boolean re = false;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:user.db")) {

            // 检查用户名是否已经存在
            String checkUserExistsQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(checkUserExistsQuery)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                int count = rs.getInt(1);
                if (count > 0) {
                    re = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
        return re;
    }

    private static void lockAccount(String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:user.db")) {
            String lockAccountQuery = "UPDATE users SET locked = 1 WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(lockAccountQuery)) {
                pstmt.setString(1, username);
                pstmt.executeUpdate();
                System.out.println("账户已被锁定，请联系管理员解锁！");
            }
        } catch (SQLException e) {
            System.out.println("数据库连接错误：" + e.getMessage());
        }
    }

    public static boolean verifyPassword(String username, String password) {
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

    //测试
    public static void main(String[] args) {
       // userLoginStart();
    }
}
