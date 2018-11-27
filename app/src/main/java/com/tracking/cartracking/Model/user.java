package com.tracking.cartracking.Model;

/**
 * Created by locall on 2/14/2018.
 */

public class user {
    private String email;
    private String password;
    private String typeUser;
    private String city;
    private String name_org;
    public user(){}


    public user(String email, String password, String typeUser, String city, String name_org) {
        this.email = email;
        this.password = password;
        this.typeUser = typeUser;
        this.city = city;

        this.name_org = name_org;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName_org() {
        return name_org;
    }

    public void setName_org(String name_org) {
        this.name_org = name_org;
    }

    public String getPassword() {
        return password;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public void setEmail(String username) {
        this.email = username;
    }
}