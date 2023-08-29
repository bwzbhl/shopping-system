package org.example;

public class CustomerInfo {
    private String customerName;
    private String customerId;
    private String customerLevel;
    private String registrationDate;
    private double totalPurchaseAmount;
    private String phoneNumber;
    private String email;

    // 构造函数
    public CustomerInfo(String customerName, String customerId, String customerLevel, String registrationDate,
                        double totalPurchaseAmount, String phoneNumber, String email) {
        this.customerName = customerName;
        this.customerId = customerId;
        this.customerLevel = customerLevel;
        this.registrationDate = registrationDate;
        this.totalPurchaseAmount = totalPurchaseAmount;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // 获取用户名
    public String getCustomerName() {
        return customerName;
    }

    // 设置用户名
    public void setCustomername(String customerName) {
        this.customerName = customerName;
    }

    // 获取客户ID
    public String getCustomerId() {
        return customerId;
    }

    // 设置客户ID
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    // 获取用户级别
    public String getCustomerLevel() {
        return customerLevel;
    }

    // 设置用户级别
    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    // 获取注册时间
    public String getRegistrationDate() {
        return registrationDate;
    }

    // 设置注册时间
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    // 获取累计消费总金额
    public double getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    // 设置累计消费总金额
    public void setTotalPurchaseAmount(double totalPurchaseAmount) {
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    // 获取手机号
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // 设置手机号
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // 获取邮箱
    public String getEmail() {
        return email;
    }

    // 设置邮箱
    public void setEmail(String email) {
        this.email = email;
    }
}
