package com.example.mybank.Controllers.Admin;

import java.time.LocalDate;

public class User
{
    private int user_id;
    private String userName;
    private String phone;
    private String email;
    private String address;
    private LocalDate birthday;
    private String password;
    private String usertype;

    public User(int user_id, String userName, String phone, String email, String address, LocalDate birthday, String password,String usertype)
    {
        this.user_id = user_id;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.birthday = birthday;
        this.password = password;
        this.usertype=usertype;

    }

    public int getUser_id()
    {
        return user_id;
    }

    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }
    public String getUserName()
    {
        return userName;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public String getPhone()
    {
        return phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
    public LocalDate getBirthday()
    {
        return birthday;
    }
    public void setBirthday(LocalDate birthday)
    {
        this.birthday = birthday;
    }
    public String getUsertype()
    {
        return usertype;
    }
    public void setUsertype(String usertype)
    {
        this.usertype = usertype;
    }

}
