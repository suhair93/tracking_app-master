package com.tracking.cartracking.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abu Ghazi on 14/9/2018.
 */

public class NotificationMSG {

    public String emailEmp;
    public String msg;
    public String date;

    public NotificationMSG() {
    }

    public String getEmailEmp() {
        return emailEmp;
    }

    public void setEmailEmp(String emailEmp) {
        this.emailEmp = emailEmp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
