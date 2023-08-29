package org.example;

import java.util.Scanner;

//主程序
public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        home();
    }

    //主页
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
}