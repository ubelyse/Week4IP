package com.example.eventrese.models;

public class User {
    private String id;
    private String username;
    private String fullName;
    private String address;
    private String imageURL;
    private String description;
    private String phoneNumber;
    private String dateOfBirth;
    private String status;
    private String search;

    public User(String id, String username, String fullName, String address,String imageUR, String description, String phoneNumber, String dateOfBirth, String status, String search) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.address = address;
        this.imageURL=imageUR;
        if(description.isEmpty())
            this.description = "Description not yet";
        else
            this.description = description;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.status=status;
        this.search = search;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
