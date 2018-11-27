package com.tracking.cartracking.Model;

import com.google.gson.annotations.SerializedName;

public class EmpModel {
    @SerializedName("ID")
    public String ID;
    @SerializedName("Name")
    public String Name;
    @SerializedName("Number")
    public String Number;
    @SerializedName("phone")
    public String phone;
    @SerializedName("car_no")
    public String car_no;

    @SerializedName("car_type")
    public String car_type;

    public EmpModel(String ID, String name, String number, String phone, String car_no, String car_type) {
        this.ID = ID;
        Name = name;
        Number = number;
        this.phone = phone;
        this.car_no = car_no;
        this.car_type = car_type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCar_no() {
        return car_no;
    }

    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }
}
