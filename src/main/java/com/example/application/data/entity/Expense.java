package com.example.application.data.entity;

public class Expense {
    private String category;
    private String title;
    private Double amount;
    private String payedWith;
    private String date;

    public Expense(String category, String title, Double amount, String payedWith, String date) {
        this.category = category;
        this.title = title;
        this.amount = amount;
        this.payedWith = payedWith;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.category = title;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void addAmount(Double new_amount) {
        this.amount += new_amount;
    }
}

