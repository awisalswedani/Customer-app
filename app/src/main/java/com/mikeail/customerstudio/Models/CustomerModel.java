package com.mikeail.customerstudio.Models;

public class CustomerModel {
    private String customerKey;
    private String customerName;
    private String customerGender;
    private String customerAge;
    private String customerPhone;
    private String customerCompany;
    private String customerCountry;

    public CustomerModel(String customerKey, String customerName, String customerGender, String customerAge, String customerPhone, String customerCompany, String customerCountry) {
        this.customerKey = customerKey;
        this.customerName = customerName;
        this.customerGender = customerGender;
        this.customerAge = customerAge;
        this.customerPhone = customerPhone;
        this.customerCompany = customerCompany;
        this.customerCountry = customerCountry;
    }

    public CustomerModel() {
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public String getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(String customerAge) {
        this.customerAge = customerAge;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerCompany() {
        return customerCompany;
    }

    public void setCustomerCompany(String customerCompany) {
        this.customerCompany = customerCompany;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }
}
