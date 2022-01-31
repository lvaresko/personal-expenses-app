package com.example.application.data.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.cloud.Timestamp;

public class Expense {
    Timestamp date;
    private String category;
    private String title;
    private Double amount;
    private String payedWith;

    public Expense(String category, String title, Double amount, String payedWith, Timestamp date) {
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

    public String getPayedWith() {
        return payedWith;
    }

    public void setPayedWith(String payedWith) {
        this.payedWith = payedWith;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void addAmount(Double new_amount) {
        this.amount += new_amount;
    }

    public String getDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy.");
        Date date = this.date.toDate();
        return formatter.format(date);

    }
}

