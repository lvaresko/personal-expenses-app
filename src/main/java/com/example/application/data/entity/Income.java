package com.example.application.data.entity;

public class Income {
    private String id;
    private String name;
    private Double amount;

    public Income() {}

    public Income(Double amount, String name,String id) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String incomeName) {
        this.name = incomeName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount += amount;
    }

    public String getId() {
        return id;
    }
}
