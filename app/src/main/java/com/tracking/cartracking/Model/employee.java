package com.tracking.cartracking.Model;

public class employee {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String car_no;
    private String car_type;
    private String name_org;
    private String admin;

    public employee() {
    }

    public employee(String email, String password, String name, String phone, String car_no, String car_type) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.car_no = car_no;
        this.car_type = car_type;
    }

    public String getAdmin() {
        return admin;
    }

    public String getName_org() {
        return name_org;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setName_org(String name_org) {
        this.name_org = name_org;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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