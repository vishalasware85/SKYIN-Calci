package com.skyncalci.Model;

public class Data {

    private String name, email, image, referCode, pla,mob,purDat;
    private int coins,checkinDay;

    public Data() {
    }

    public Data(String name, String email,String image,String referCode, String pla, String mob,String purDat,int coins,int checkinDay) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.referCode = referCode;
        this.pla=pla;
        this.mob=mob;
        this.purDat=purDat;
        this.coins = coins;
        this.checkinDay=checkinDay;
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

    public int getCoins() {
        return coins;
    }
    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getReferCode() {
        return referCode;
    }
    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getPla() {
        return pla;
    }
    public void setPla(String pla) {
        this.pla = pla;
    }

    public String getMob() {
        return mob;
    }

    public String getPurDat() {
        return purDat;
    }
    public void setPurDat(String purDat) {
        this.purDat = purDat;
    }

    public int getCheckinDay() {
        return checkinDay;
    }
}
