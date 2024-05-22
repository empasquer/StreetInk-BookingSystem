package com.example.streetinkbookingsystem.models;

public class Client extends Person implements Comparable<Client>{
    private int id;
    private String description;

    public Client (){
        //super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @author Munazzah
     * @param o the object to be compared.
     * @return int
     */
    @Override
    public int compareTo(Client o) {
        return this.getFirstName().compareTo(o.getFirstName());
    }
}
