package com.example.mybank.Controllers.Admin;

public class Account
{
    private int account_id;
    private int user_id;
    private String account_number;
    private String account_type;
    private int balance;
    private int pin;
    private String status;

    public Account(int account_id,int user_id,String account_number,String account_type,int balance,int pin,String status)
    {
        this.account_id=account_id;
        this.user_id=user_id;
        this.account_number=account_number;
        this.account_type=account_type;
        this.balance=balance;
        this.pin=pin;
        this.status=status;
    }

    public int getAccount_id(){return account_id;}
    public void setAccount_id(int account_id){this.account_id=account_id;}
    public int getUser_id(){return user_id;}
    public void setUser_id(int user_id){this.user_id=user_id;}
    public String getAccount_number(){return account_number;}
    public void setAccount_number(String account_number){this.account_number=account_number;}
    public String getAccount_type(){return account_type;}
    public void setAccount_type(String account_type){this.account_type=account_type;}
    public int getBalance(){return balance;}
    public void setBalance(int balance){this.balance=balance;}
    public int getPin(){return pin;}
    public void setPin(int pin){this.pin=pin;}
    public String getStatus(){return    status;}
    public void setStatus(String status){this.status=status;}


}

