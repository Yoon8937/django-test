package com.example.redzone.model;

public class PostModel {

    private Integer userid;
    private float lat;
    private float lng;

    public PostModel(Integer name, float lat, float lng){
        this.userid = name;
        this.lat = lat;
        this.lng = lng;
    }
}