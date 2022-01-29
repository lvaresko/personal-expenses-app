package com.example.application.views.expenses;

public class ExpenseItem {
    private Integer id;
    private String category;
    private String title;
    private Double amount;

    protected ExpenseItem(Integer id, String category, String title, Double amount) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.amount = amount;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
