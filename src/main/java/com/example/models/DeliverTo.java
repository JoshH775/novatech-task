package com.example.models;

import java.util.List;

public class DeliverTo {
    private String name;
    private List<String> address_lines;
    private String postal_code;
    private String email_address;
    private String phone_number;

    public DeliverTo(List<String> address_lines, String email_address, String name, String phone_number, String postal_code) {
        this.address_lines = address_lines;
        this.email_address = email_address;
        this.name = name;
        this.phone_number = phone_number;
        this.postal_code = postal_code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAddress_lines() {
        return address_lines;
    }

    public void setAddress_lines(List<String> address_lines) {
        this.address_lines = address_lines;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    @Override
    public String toString() {
        return super.toString();
    }
    
    public String toPrettyString() {
        return "DeliverTo{" +
                "name='" + name + '\'' +
                ", address_lines=" + address_lines +
                ", postal_code='" + postal_code + '\'' +
                ", email_address='" + email_address + '\'' +
                ", phone_number='" + phone_number + '\'' +
                '}';

    }
}
