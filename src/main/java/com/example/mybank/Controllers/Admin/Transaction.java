package com.example.mybank.Controllers.Admin;


import java.time.LocalDate;

public class Transaction
{
    private int id;
    private int account_id;
    private String tran_type;
    private String amount;
    private LocalDate tran_date;
    private String bank;
    private String payee_address;
    private String received_name;
    private String status;
    private String action;

    public Transaction(int id,int account_id,String tran_type,String amount,LocalDate tran_date,
                       String bank,String payee_address,String received_name,String status,String action)
    {
        this.id=id;
        this.account_id=account_id;
        this.tran_type=tran_type;
        this.tran_date =tran_date;
        this.amount=amount;
        this.bank=bank;
        this.payee_address=payee_address;
        this.received_name=received_name;
        this.status=status;
        this.action=action;
    }

    public int getId() {
        return id;
    }
    public void setId(int id)
    {
        this.id=id;
    }
    public int getAccount_id(){
        return account_id;
    }
    public void setAccount_id(int account_id)
    {
        this.account_id=account_id;
    }
    public String getTran_type(){
        return tran_type;
    }
    public void setTran_type(String tran_type)
    {
        this.tran_type=tran_type;
    }
    public String getAmount(){
        return amount;
    }
    public void setAmount(String amount)
    {
        this.amount=amount;
    }
    public LocalDate getTran_date(){
        return tran_date;
    }
    public void setTran_date(LocalDate tran_date)
    {
        this.tran_date=tran_date;
    }
    public String getBank()
    {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getPayee_address() {
        return payee_address;
    }

    public void setPayee_address(String payee_address) {
        this.payee_address = payee_address;
    }

    public String getReceived_name() {
        return received_name;
    }

    public String getStatus() {
        return status;
    }

    public void setReceived_name(String received_name) {
        this.received_name = received_name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAction(){
        return action;
    }
    public void setAction(){
        this.action=action;
    }
}
