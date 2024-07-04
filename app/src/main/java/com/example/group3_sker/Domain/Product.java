package com.example.group3_sker.Domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    @SerializedName("product-id")
    private String id;
    @SerializedName("product-name")
    private String name;
    private String description;
    @SerializedName("picture-url")
    private String picUrl;
    private int review;
    private double score;
    private int numberInCart;
    private double price;

    public Product(String id, String name, String description, String picUrl, int review, double score, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picUrl = picUrl;
        this.review = review;
        this.score = score;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public double getPrice() {
        return price/23000;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
