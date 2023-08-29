package org.example;

public class ItemInfo {
    private int itemId;
    private String itemName;
    private String manufacturer;
    private String productionDate;
    private String model;
    private double purchasePrice;
    private double retailPrice;
    private int quantity;

    // 构造函数
    public ItemInfo(int productId, String productName, String manufacturer, String productionDate, String model,
                    double purchasePrice, double retailPrice, int quantity) {
        this.itemId = productId;
        this.itemName = productName;
        this.manufacturer = manufacturer;
        this.productionDate = productionDate;
        this.model = model;
        this.purchasePrice = purchasePrice;
        this.retailPrice = retailPrice;
        this.quantity = quantity;
    }

    // 获取商品编号
    public int getItemId() {
        return itemId;
    }

    // 设置商品编号
    public void setItemId(int productId) {
        this.itemId = productId;
    }

    // 获取商品名称
    public String getItemName() {
        return itemName;
    }

    // 设置商品名称
    public void setItemName(String productName) {
        this.itemName = productName;
    }

    // 获取生产厂家
    public String getManufacturer() {
        return manufacturer;
    }

    // 设置生产厂家
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    // 获取生产日期
    public String getProductionDate() {
        return productionDate;
    }

    // 设置生产日期
    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    // 获取型号
    public String getModel() {
        return model;
    }

    // 设置型号
    public void setModel(String model) {
        this.model = model;
    }

    // 获取进货价
    public double getPurchasePrice() {
        return purchasePrice;
    }

    // 设置进货价
    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    // 获取零售价格
    public double getRetailPrice() {
        return retailPrice;
    }

    // 设置零售价格
    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    // 获取数量
    public int getQuantity() {
        return quantity;
    }

    // 设置数量
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}