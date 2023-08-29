package org.example;

import java.util.Scanner;

public class UserSystem {
    private UserLogin userLogin = new UserLogin();
    private UserRegistration userRegistration = new UserRegistration();
    static Scanner scanner = new Scanner(System.in);

    public void userSystemStart() {
        System.out.println("-----当前为用户系统-----");
        System.out.println("1. 用户注册");
        System.out.println("2. 用户登录");
        System.out.println("请选择");

        int choice = scanner.nextInt();
        if(choice == 1)
            userRegistration.registrationStart();
        else userLogin.userLoginStart();
    }
}
