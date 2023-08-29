package org.example;

import java.util.*;

public class AfterUserLogin {
    private static UserPasswordManage userPasswordManage = new UserPasswordManage();
    private static ShoppingCart shoppingCart = new ShoppingCart();
    static Scanner scanner = new Scanner(System.in);
    public void afterUserLogin(String username) {

        while (true) {
            System.out.println("--------这里是用户登录后---------");
            System.out.println("请选择您要要进行的操作");
            System.out.println("1. 密码管理");
            System.out.println("2. 进入购物");
            System.out.println("3. 退出登录");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    userPasswordManage.userPassowrdManageStart(username);
                    break;
                case 2:
                    shoppingCart.shoppingStart(username);
                    break;
                case 3:
                    logout();
                    break;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    private static void logout() {
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
}
