package org.example;

import java.util.Scanner;

public class AfterAdminLogin {
    private static Scanner scanner = new Scanner(System.in);
    private static AdminPasswordManage adminPasswordManage = new AdminPasswordManage();
    private static CustomerManage customerManage = new CustomerManage();
    private static ItemManage itemManage = new ItemManage();

    public void afterAdminLogin() {

        while (true) {
            System.out.println("-----这里是管理员登录后------");
            System.out.println("1. 密码管理");
            System.out.println("2. 客户管理");
            System.out.println("3. 商品管理");
            System.out.println("4. 退出登录");
            System.out.println("请选择:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    adminPasswordManage.adminPasswordManageStart();
                    break;
                case 2:
                    customerManage.customerManageStart();
                    break;
                case 3:
                    itemManage.itemManageStart();
                    break;
                case 4:
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
