package com.tracking.cartracking.Model;

public class EmpModelReg {

    public String email;
    public String admin;
    public String car;
    public String name;
    public String arrivel;
    public String departure;

    public EmpModelReg() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArrivel() {
        return arrivel;
    }

    public String getDeparture() {
        return departure;
    }

    public void setArrivel(String arrivel) {
        this.arrivel = arrivel;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }
}