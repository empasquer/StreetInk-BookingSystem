package com.example.streetinkbookingsystem.models;

public class Client extends Person {
    private String clientDesc;

    public Client (){

    }

    public String getClientDesc() {
        return clientDesc;
    }

    public void setClientDesc(String clientDesc) {
        this.clientDesc = clientDesc;
    }
}
