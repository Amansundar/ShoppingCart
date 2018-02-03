package com.interview.android.dto;

import com.interview.android.dto.ProductObject;

/**
 * Created by SESA383212 on 14-02-2016.
 */
public class PurchaseObject {

    private String date;
    private double total;
    private ProductObject productArray;


    public PurchaseObject(String date, double total, ProductObject productArray){
        this.date = date;
        this.total = total;
        this.productArray=productArray;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ProductObject getProductArray() {
        return productArray;
    }





}
