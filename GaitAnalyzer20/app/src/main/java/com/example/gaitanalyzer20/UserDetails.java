package com.example.gaitanalyzer20;

public class UserDetails {
    String name;
    String username;
    Long contact;
    int age;
    String gender;
    String occupation;
    UserDetails(){

    }

    public UserDetails(String name, String username, Long contact, int age, String gender, String occupation) {
        this.name = name;
        this.username = username;
        this.contact = contact;
        this.age = age;
        this.gender = gender;
        this.occupation = occupation;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public Long getContact() {
        return contact;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getOccupation() {
        return occupation;
    }
}
