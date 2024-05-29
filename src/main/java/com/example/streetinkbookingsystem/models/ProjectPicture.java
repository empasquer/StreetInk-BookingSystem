package com.example.streetinkbookingsystem.models;
/**
 * @author everyone
 */
public class ProjectPicture {
    private int id;
    private byte[] pictureData;
    private int bookingId;

    public ProjectPicture(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPictureData() {
        return pictureData;
    }

    public void setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
}
