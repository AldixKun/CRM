package com.example.gamecrm.dto;

public class CartItem {
    private String productId;
    private int cantidad;

    public CartItem(){}

    public CartItem(String productId, int cantidad){
        this.productId = productId;
        this.cantidad = cantidad;
    }

    public String getProductId(){ return productId; }
    public void setProductId(String productId){ this.productId=productId; }

    public int getCantidad(){ return cantidad; }
    public void setCantidad(int cantidad){ this.cantidad=cantidad; }
}
