package com.rleon.springbatchp01demo.model.entity;


import lombok.Data;

@Data
public class People {

    private String firstName;
    private String secondName;
    private String phone;

    public People(String firstName, String secondName, String phone) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "People{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
