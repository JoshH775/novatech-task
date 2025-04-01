package com.example.models;

public final class Person {
    private String name;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String postalCode;
    private String email;
    private String phoneNumber;

    // Constructor
    public Person(String name, String address1, String address2, String address3, String address4, String postalCode, String email, String phoneNumber) {
        setName(name);
        setAddress1(address1);
        setAddress2(address2);  // Can be nullable
        setAddress3(address3);  // Can be nullable
        setAddress4(address4);  // Can be nullable
        setPostalCode(postalCode);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (name == null || name.length() < 5) {
            throw new IllegalArgumentException("Name must be at least 5 characters long");
        }
        if (name.length() > 30) {
            throw new IllegalArgumentException("Name must be at most 30 characters long");
        }
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }
    public void setAddress1(String address1) {
        if (address1 == null || address1.isEmpty()) {
            throw new IllegalArgumentException("Address1 cannot be null or empty");
        }
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }
    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        if (postalCode != null && postalCode.length() > 8) {
            throw new IllegalArgumentException("Postal code must be at most 8 characters long");
        }
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if (email != null && email.length() > 254) {
            throw new IllegalArgumentException("Email must be at most 254 characters long");
        }
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() > 30) {
            throw new IllegalArgumentException("Phone number must be at most 30 characters long");
        }
        this.phoneNumber = phoneNumber;
    }
}
