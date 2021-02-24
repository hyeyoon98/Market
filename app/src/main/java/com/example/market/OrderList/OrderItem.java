package com.example.market.OrderList;

import com.google.gson.annotations.SerializedName;

public class OrderItem {

    @SerializedName("marketName")
    private String marketName;

    @SerializedName("order_content")
    private String orderContent;

    @SerializedName("regist_date")
    private String date;

    @SerializedName("login_id")
    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    /*public OrderItem(String marketName, String orderContent, String date, String userID){
        this.marketName = marketName;
        this.orderContent = orderContent;
        this.date = date;
        this.userID = userID;
    }*/

    @Override
    public String toString() {
        return "OrderItem{" +
                "marketName='" + marketName + '\'' +
                ", orderContent='" + orderContent + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
