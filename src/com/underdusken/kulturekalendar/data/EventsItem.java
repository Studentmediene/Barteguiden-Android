package com.underdusken.kulturekalendar.data;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 9/26/12
 * Time: 8:36 PM
 *  Event item
 */
public class EventsItem {
    private long id = 0;
    private String eventsId = "0";
    private String name = "";
    private String type = "";
    private String address = "";
    private boolean isGeo = true;
    private float geoLatitude = 0.0f;
    private float geoLongitude = 0.0f;
    private String dateStart = "";
    private String dateEnd = "";
    private String showDate = "";   // date witch would show to user,  some events have period and because of this we must some times calculate date witch we want to show
    private float price =  0.0f;
    private int ageLimit = 0;
    private boolean favorite = false;
    private float beerPrice = 0.0f;
    private String descriptionEnglish = "";
    private String descriptionNorwegian = "";
    // Images
    private String picture = "";
    private String smallPicture = "";

    private String weekendRecommendationEnglish = "";
    private String weekendRecommendationNorwegian = "";

    private int notificationId = 0;


    // Constructors
    public EventsItem(){

    }

    public EventsItem(String name, String date){
        this.name = name;
        this.dateStart = date;
    }


    // Set methods
    public void setEventsId(String eventsId){
        this.eventsId = eventsId;
    }
    public void setId(long id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setGeoLatitude(float geoLatitude){
        this.geoLatitude = geoLatitude;
    }
    public void setGeoLongitude(float geoLongitude){
        this.geoLongitude = geoLongitude;
    }
    public void setDateStart(String date){
        this.dateStart = date;
    }
    // TODO
    public void setShowDate(String date){
        this.showDate = date;
    }
    public void setDateEnd(String date){
        this.dateEnd = date;
    }
    public void setPrice(float price){
        this.price = price;
    }
    public void setAgeLimit(int ageLimit){
        this.ageLimit = ageLimit;
    }
    public void setFavorite(boolean favorite){
        this.favorite = favorite;
    }
    public void setBeerPrice(float beerPrice){
        this.beerPrice = beerPrice;
    }
    public void setDescriptionEnglish(String descriptionEnglish){
        this.descriptionEnglish = descriptionEnglish;
    }
    public void setDescriptionNorwegian(String descriptionNorwegian){
        this.descriptionNorwegian = descriptionNorwegian;
    }
    public void setPicture(String picture){
        this.picture = picture;
    }
    public void setSmallPicture(String smallPicture){
        this.smallPicture = smallPicture;
    }
    public void setWeekendRecommendationEnglish(String weekendRecommendationEnglish){
        this.weekendRecommendationEnglish = weekendRecommendationEnglish;
    }
    public void setWeekendRecommendationNorwegian(String weekendRecommendationNorwegian){
        this.weekendRecommendationNorwegian = weekendRecommendationNorwegian;
    }

    public void setNotificationId(int notificationId){
        this.notificationId = notificationId;
    }



    // Get methods
    public long getId(){
        return this.id;
    }
    public String getEventsId(){
        return this.eventsId;
    }
    public String getName(){
        return this.name;
    }
    public String getType(){
        return this.type;
    }
    public String getAddress(){
        return this.address;
    }
    public float getGeoLatitude(){
        return this.geoLatitude;
    }
    public float getGeoLongitude(){
        return this.geoLongitude;
    }
    public String getDateStart(){
        return this.dateStart;
    }
    public String getDateEnd(){
        return this.dateEnd;
    }
    public String getShowDate(){
        return this.showDate;
    }
    public float getPrice(){
        return this.price;
    }
    public int getAgeLimit(){
        return this.ageLimit;
    }
    public boolean getFavorite(){
        return this.favorite;
    }
    public float getBeerPrice(){
        return this.beerPrice;
    }
    public String getDescriptionEnglish(){
        return this.descriptionEnglish;
    }
    public String getDescriptionNorwegian(){
        return this.descriptionNorwegian;
    }
    public String getPicture(){
        return this.picture;
    }
    public String getSmallPicture(){
        return this.smallPicture;
    }
    public String getWeekendRecommendationEnglish(){
        return this.weekendRecommendationEnglish;
    }
    public String getWeekendRecommendationNorwegian(){
        return this.weekendRecommendationNorwegian;
    }
    public int getNotificationId(){
        return this.notificationId;
    }


}
