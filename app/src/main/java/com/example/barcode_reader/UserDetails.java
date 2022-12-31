package com.example.barcode_reader;

public class UserDetails {
    String name;
    String email;
    String phone;
    String BName;
    String Blogo;
    String BAddress;
    String Burl;
    String Bloc;

    public UserDetails() {
    }

    public UserDetails(String name, String email, String phone, String BName, String blogo, String BAddress, String burl, String bloc) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.BName = BName;
        Blogo = blogo;
        this.BAddress = BAddress;
        Burl = burl;
        Bloc = bloc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBName() {
        return BName;
    }

    public void setBName(String BName) {
        this.BName = BName;
    }

    public String getBlogo() {
        return Blogo;
    }

    public void setBlogo(String blogo) {
        Blogo = blogo;
    }

    public String getBAddress() {
        return BAddress;
    }

    public void setBAddress(String BAddress) {
        this.BAddress = BAddress;
    }

    public String getBurl() {
        return Burl;
    }

    public void setBurl(String burl) {
        Burl = burl;
    }

    public String getBloc() {
        return Bloc;
    }

    public void setBloc(String bloc) {
        Bloc = bloc;
    }
}
