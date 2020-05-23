//# COMP 4521    #  CHEN ZIYI       20319433          zchenbu@connect.ust.hk
//# COMP 4521    #  FENG ZIHAN      20412778          zfengae@ust.uk

package com.example.wander_mine;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Attraction {
    private int id;

    private String name;

    @JsonProperty("LatLng")
    private double[] LatLng;

    private String info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getLatLng() {
        return LatLng;
    }

    public void setLatLng(double[] LatLng) {
        this.LatLng = LatLng;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
