package org.example;

import java.util.Scanner;

public class MyAboutAction implements MyAction {
    private static final String ACTION_NAME = "about";

    private Scanner scanner = null;

    public MyAboutAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String getActionName() {
        return MyAboutAction.ACTION_NAME;
    }

    @Override
    public void run(String[] args) {

        String userInput = null;

        while (true) {
            System.out.println("这个是一个About，就是自我介绍的意思，输入 q 返回上级菜单");

            userInput = this.scanner.nextLine();

            if (userInput.equals("q")) {
                break;
            }
        }
    }

}
