package com.example.mylib;
class User {
    public String Userid;
    public String Email;
    public String password;
    public String books;
    public String issue_date;
    public User() {

    }

    public User(String userid, String email, String password) {
        this.Userid = userid;
        this.Email = email;
        this.password = password;
        this.books ="";
        this.issue_date="";
    }
}
