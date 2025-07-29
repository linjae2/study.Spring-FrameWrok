package com.dohbedoh.model;

/**
 * Simple Company: A name, a list of employees.
 * <p>
 * Created by Allan on 1/10/2015.
 */
public class Company {

    private int companyId;
    private String companyName;
    private int addressId;
    private int contactId;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
