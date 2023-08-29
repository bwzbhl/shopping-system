package org.example;

public class AdminSystem {

    private AdminLogin adminLogin = new AdminLogin();

    public void adminSystemStart() {
        System.out.println("-----当前为管理员系统-----");
        System.out.println("请登录管理员账号");
        adminLogin.adminLoginStart();
    }
}
