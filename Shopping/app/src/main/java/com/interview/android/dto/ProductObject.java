package com.interview.android.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SESA383212 on 14-02-2016.
 */
public class ProductObject {

    private String name;
    private int id;
    private double price;
    private boolean isEnabled=false;
    private int count;
    private String date=null;

    public ProductObject(int id,String name,double price){
        this.id = id;
        this.name = name;
        this.price=price;
        Date date = new Date(java.lang.System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        this.date = sdf.format(date);
    }

    public ProductObject(int id,String name,double price,String dates,int count){
        this.id = id;
        this.name = name;
        this.price=price;
       this.count=count;
        this.isEnabled=true;
        this.date = dates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



}
