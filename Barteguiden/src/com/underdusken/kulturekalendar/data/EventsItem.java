package com.underdusken.kulturekalendar.data;

import com.underdusken.kulturekalendar.utils.SimpleTimeFormat;

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
    private String title = "";

    private String categoryID = "";
    private String address = "";
    private boolean isGeo = false;
    private float geoLatitude = 200.0f;
    private float geoLongitude = 200.0f;
    private String dateStart = "";
    private long dateStartMS = 0;
    private int price =  0;
    private String placeName = "";
    private String showDate = "";   // date witch would show to user,  some events have period and because of this we must some times calculate date witch we want to show
    private int ageLimit = 0;

    private boolean favorite = false;
    private float beerPrice = 0.0f;
    private String descriptionEnglish = "";
    private String descriptionNorwegian = "";
    // Images
    private String imageURL = "";
    private String eventURL = "";
    public boolean isRecommended = false;
    private int notificationId = 0;


    // Constructors
    public EventsItem(){

    }

    public EventsItem(String title, String date){
        this.title = title;
        this.dateStart = date;
        this.dateStartMS = new SimpleTimeFormat(date).getMs();
    }


    // Set methods
    public void setPlaceName(String placeName){
        this.placeName = placeName;
    }
    public void setisRecomended(boolean isRecomended){
        this.isRecommended = isRecomended;
    }
    public void setEventsId(String eventsId){
        this.eventsId = eventsId;
    }
    public void setEventURL(String eventURL){
        this.eventURL = eventURL;
    }
    public void setId(long id){
        this.id = id;
    }
    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }
    public void setTitle(String name){
        this.title = name;
    }
    public void setCategoryID(String categoryID){
        this.categoryID = categoryID;
    }
    public void setIsGeo(boolean isGeo){
        this.isGeo = isGeo;
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
        this.dateStartMS = new SimpleTimeFormat(date).getMs();
    }
    // TODO
    public void setShowDate(String date){
        this.showDate = date;
    }
    public void setPrice(int price){
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

    public void setNotificationId(int notificationId){
        this.notificationId = notificationId;
    }



    // Get methods
    public boolean getIsRecommended(){
        return this.isRecommended;
    }
    public String getPlaceName(){
        return this.placeName;
    }
    public String getEventURL(){
        return this.eventURL;
    }
    public long getId(){
        return this.id;
    }
    public boolean getIsGeo(){
        if(this.getGeoLatitude() < 200.0f)
            return true;
        else
            return false;
    }
    public long getDateStartMS(){
        return this.dateStartMS;
    }

    public String getImageURL(){
        return this.imageURL;
    }
    public String getEventsId(){
        return this.eventsId;
    }
    public String getTitle(){
        return this.title;
    }
    public String getCategoryID(){
        return this.categoryID;
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

    public int getNotificationId(){
        return this.notificationId;
    }


}
