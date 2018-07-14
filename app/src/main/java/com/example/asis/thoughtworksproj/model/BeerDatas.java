package com.example.asis.thoughtworksproj.model;

import com.google.gson.annotations.SerializedName;

public class BeerDatas {

    @SerializedName("abv")
    private String mAlcoholCont;
    @SerializedName("ibu")
    private String mDrinkBitterness;
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("style")
    private String mBeerStyle;
    @SerializedName("ounces")
    private String mOunces;

    public BeerDatas(String alcoholCont, String drinkBitterness, String id, String name, String beerStyle, String ounces) {
        mAlcoholCont = alcoholCont;
        mDrinkBitterness = drinkBitterness;
        mId = id;
        mName = name;
        mBeerStyle = beerStyle;
        mOunces = ounces;
    }


    public String getmAlcoholCont() {
        if(!mAlcoholCont.equalsIgnoreCase(""))
        return mAlcoholCont;
        else{
            return "0.0";
        }
    }

    public Double getmAlcoholContInDouble() {
        if(mAlcoholCont!=null && (!mAlcoholCont.equalsIgnoreCase("")))
        return Double.valueOf(mAlcoholCont);
        else
            return 0.0;
    }

    public void setmAlcoholCont(String mAlcoholCont) {
        this.mAlcoholCont = mAlcoholCont;
    }

    public String getmDrinkBitterness() {
        return mDrinkBitterness;
    }

    public void setmDrinkBitterness(String mDrinkBitterness) {
        this.mDrinkBitterness = mDrinkBitterness;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmBeerStyle() {
        return mBeerStyle;
    }

    public void setmBeerStyle(String mBeerStyle) {
        this.mBeerStyle = mBeerStyle;
    }

    public String getmOunces() {
        return mOunces;
    }

    public void setmOunces(String mOunces) {
        this.mOunces = mOunces;
    }
}
