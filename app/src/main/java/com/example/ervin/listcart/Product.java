package com.example.ervin.listcart;

public class Product {
    public String productName;
    public String productId;
    public int productPrice;
    public int productQty;

    public String getNamaProduct() {
        return productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public Product(String productName, String productId, int productPrice, int productQty) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productId = productId;
        this.productQty = productQty;
    }


    public int getHargaProduct() {
        return productPrice;
    }

}
