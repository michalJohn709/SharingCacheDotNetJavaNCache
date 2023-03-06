package org.example;

import java.io.Serializable;

public class Customer implements Serializable
{
    private String CustomerID;
    private String ContactName;
    private String CompanyName;
    private String ContactNo;
    private String ContactTitle;
    private String Address;
    private String City;
    private String Country;
    private String region;
    private String phone;

    public String getContactTitle() {
        return ContactTitle;
    }

    public void setContactTitle(String contactTitle) {
        ContactTitle = contactTitle;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String postalCode;
    private String fax;

    public Customer()
    {
    }

    /**
     Unique Id of the customer
     */
    public String getCustomerID()
    {
        return CustomerID;
    }
    public void setCustomerID(String value)
    {
        CustomerID = value;
    }

    /**
     Contact name of the customer
     */
    public String getContactName()
    {
        return ContactName;
    }
    public void setContactName(String value)
    {
        ContactName = value;
    }

    /**
     Company the customer works for
     */
    public String getCompanyName()
    {
        return CompanyName;
    }
    public void setCompanyName(String value)
    {
        CompanyName = value;
    }

    /**
     Contact number of the customer
     */
    public String getContactNo()
    {
        return ContactNo;
    }
    public void setContactNo(String value)
    {
        ContactNo = value;
    }

    /**
     Residential address of the customer
     */
    public String getAddress()
    {
        return Address;
    }
    public void setAddress(String value)
    {
        Address = value;
    }

    /**
     Residence city of the customer
     */
    public String getCity()
    {
        return City;
    }
    public void setCity(String value)
    {
        City = value;
    }

    /**
     Nationality of the customer
     */
    public String getCountry()
    {
        return Country;
    }
    public void setCountry(String value)
    {
        Country = value;
    }

    /**
     Postal code of the customer
     */
    public String getPostalCode()
    {
        return postalCode;
    }
    public void setPostalCode(String value)
    {
        postalCode = value;
    }

    /**
     Fax number of the customer
     */
    public String getFax()
    {
        return fax;
    }
    public void setFax(String value)
    {
        fax = value;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "CustomerID='" + CustomerID + '\'' +
                ", ContactName='" + ContactName + '\'' +
                ", CompanyName='" + CompanyName + '\'' +
                ", ContactNo='" + ContactNo + '\'' +
                ", ContactTitle='" + ContactTitle + '\'' +
                ", Address='" + Address + '\'' +
                ", City='" + City + '\'' +
                ", Country='" + Country + '\'' +
                ", region='" + region + '\'' +
                ", phone='" + phone + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", fax='" + fax + '\'' +
                '}';
    }
}
