package com.example.abhinav.studentfirebase;

/**
 * Created by ABHINAV on 20-05-2016.
 */
public class Event {
    /*
    id
    title
    description
    datetime
    images
    */
    private String eventid;
    private String title;
    private String description;
     String t;
    private String noofimages;
    private  String url;

    public Event() {

        title = "";
        description = "";
        t = "";
        noofimages= "";
        eventid="";
    }

    public Event(String eventid,String t, String description) {
        this.eventid = eventid;
        this.title = title;
        this.description = description;
        this.t = t;
        //this.noofimages = images;
    }

    public void sett(String t) {
        this.t = t;
    }
    public String gett() {
        return t;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }






    public String getnoofimages() {
        return noofimages;
    }

    public void setnoofmages(String images) {
        this.noofimages = images;
    }

    @Override
    public String toString() {
        return ": title = " + title + " : description = " + description + " : datetime = " + t + " : images = " + noofimages;
    }
}
