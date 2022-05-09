package com.example.readwitharti;

public class User {
    private String date;
    private String mail;
    private String name;

    public User() {
    }

    public User(String date, String mail, String name) {
        this.date = date;
        this.mail = mail;
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
