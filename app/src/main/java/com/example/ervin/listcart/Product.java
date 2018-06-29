package com.example.ervin.listcart;

public class Product {

    public String productName, productMenuImage;
    public String productId;
    public int productPrice;
    public double getProductQtyLvl;
    public int productQtyOutlet,productQty,productQtyRekomendasi;



    public String getProductMenuImage() {
        return productMenuImage;
    }

    public void setProductMenuImage(String productMenuImage) {
        this.productMenuImage = productMenuImage;
    }

    public int getProductQtyOutlet() {
        return productQtyOutlet;
    }

    public void setProductQtyOutlet(int productQtyOutlet) {
        this.productQtyOutlet = productQtyOutlet;
    }

    public double getGetProductQtyLvl() {
        return getProductQtyLvl;
    }

    public void setGetProductQtyLvl(int getProductQtyLvl) {
        this.getProductQtyLvl = getProductQtyLvl;
    }

    public int getProductQtyRekomendasi() {
        return productQtyRekomendasi;
    }

    public void setProductQtyRekomendasi(int productQtyRekomendasi) {
        this.productQtyRekomendasi = productQtyRekomendasi;
    }

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

    public Product(String productId,String productName, String productMenuImage, int productPrice, int productQtyOutlet, double getProductQtyLvl, int productQtyRekomendasi) {
        this.productId = productId;
        this.productName = productName;
        this.productMenuImage = productMenuImage;
        this.productPrice = productPrice;
        this.productQtyOutlet = productQtyOutlet;
        this.getProductQtyLvl = getProductQtyLvl;
        this.productQtyRekomendasi = productQtyRekomendasi;
    }

    public int getHargaProduct() {
        return productPrice;
    }

}
