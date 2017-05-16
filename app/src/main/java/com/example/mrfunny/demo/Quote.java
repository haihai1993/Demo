package com.example.mrfunny.demo;

import com.google.gson.annotations.SerializedName;

public class Quote {
    private String Date;
    private String Close;
    private String Volume;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getClose() {
        return Close;
    }

    public void setClose(String close) {
        Close = close;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

}
