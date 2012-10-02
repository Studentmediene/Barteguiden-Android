package com.underdusken.kulturekalendar.data;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 9/26/12
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventsItem {
    public String name = "";
    private String type = "";
    private String address = "";
    private float geoLatitude = 0.0f;
    private float geoLongitude = 0.0f;
    public String date = "";
    private float price =  0.0f;
    private int ageLimit = 0;
    private boolean favorite = false;
    private float beerPrice = 0.0f;
    private String descriptionEnglish = "";
    private String descriptionNorwegian = "";
    // Images
    private String picture = "";
    private String smallPicture = "";

    private String weekendRecommendation = "";
    private int notificationId = 0;


    public EventsItem(String name, String date){
        this.name = name;
        this.date = date;
    }

    public String getName(){
        return this.name;
    }

    public String getDate(){
        return this.date;
    }


}
