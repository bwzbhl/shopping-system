package org.example;
import java.util.Scanner;

public class MyHelpAction implements MyAction {

    private static final String ACTION_NAME = "help";
    private Scanner scanner = null;

    public MyHelpAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String getActionName() {
        return MyHelpAction.ACTION_NAME;
    }

    @Override
    public void run(String[] args) {
        System.out.print("欢迎进入帮助子菜单");

        String userInput = "";

        while(true) {
            System.out.println("请输入你的指令,q 退出");
            System.out.print("你当前在 help 的二级子目录下 >");
            userInput = this.scanner.nextLine();

            if (userInput.equals("q")) {
                break;
            }

            System.out.println("其实吧，这个也就是做个样子给你看看，让你知道怎么做二级界面罢了");
        }
    }
    
}
