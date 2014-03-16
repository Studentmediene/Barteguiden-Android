package com.underdusken.kulturekalendar.data;

import com.underdusken.kulturekalendar.utils.SimpleTimeFormat;

/**
 * Created with IntelliJ IDEA. User: pavelarteev Date: 9/26/12 Time: 8:36 PM
 * Event item
 */
public class EventItem {
    public boolean isRecommended = false;
    private long id = 0;
    private String eventsId = "0";
    private String title = "";
    private String categoryID = "";
    private String address = "";
    private boolean isGeo = false;
    private float geoLatitude = 200.0f;
    private float geoLongitude = 200.0f;
    private String dateStart = "";
    private String dateStartDay = "";
    private long dateStartMS = 0;
    private int price = 0;
    private String placeName = "";
    private String showDate = ""; // date witch would show to user, some events
    // have period and because of this we must
    // some times calculate date witch we want
    // to show
    private int ageLimit = 0;
    private boolean favorite = false;
    private float beerPrice = 0.0f;
    private String descriptionEnglish = "";
    private String descriptionNorwegian = "";
    // Images
    private String imageURL = "";
    private String eventURL = "";
    private int notificationId = 0;

    // Constructors
    public EventItem() {

    }

    public EventItem(String title, String date) {
        this.title = title;
        this.dateStart = date;
        this.dateStartDay = date.substring(0, 10);
        this.dateStartMS = new SimpleTimeFormat(date).getMs();
    }

    public void setisRecomended(boolean isRecomended) {
        this.isRecommended = isRecomended;
    }

    public String getDateStartDay() {
        return dateStartDay;
    }

    // Get methods
    public boolean getIsRecommended() {
        return this.isRecommended;
    }

    public String getPlaceName() {
        return this.placeName;
    }

    // Set methods
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getEventURL() {
        return this.eventURL;
    }

    public void setEventURL(String eventURL) {
        this.eventURL = eventURL;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsGeo() {
        return this.getGeoLatitude() < 200.0f;
    }

    public void setIsGeo(boolean isGeo) {
        this.isGeo = isGeo;
    }

    public long getDateStartMS() {
        return this.dateStartMS;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getEventsId() {
        return this.eventsId;
    }

    public void setEventsId(String eventsId) {
        this.eventsId = eventsId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getCategoryID() {
        return this.categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getGeoLatitude() {
        return this.geoLatitude;
    }

    public void setGeoLatitude(float geoLatitude) {
        this.geoLatitude = geoLatitude;
    }

    public float getGeoLongitude() {
        return this.geoLongitude;
    }

    public void setGeoLongitude(float geoLongitude) {
        this.geoLongitude = geoLongitude;
    }

    public String getDateStart() {
        return this.dateStart;
    }

    public void setDateStart(String date) {
        this.dateStart = date;
        this.dateStartMS = new SimpleTimeFormat(date).getMs();
        this.dateStartDay = date.substring(0, 10);
    }

    public String getShowDate() {
        return this.showDate;
    }

    public void setShowDate(String date) {
        this.showDate = date;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAgeLimit() {
        return this.ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;

    }

    public float getBeerPrice() {
        return this.beerPrice;
    }

    public void setBeerPrice(float beerPrice) {
        this.beerPrice = beerPrice;
    }

    public String getDescriptionEnglish() {
        return this.descriptionEnglish;
    }

    public void setDescriptionEnglish(String descriptionEnglish) {
        this.descriptionEnglish = descriptionEnglish;
    }

    public String getDescriptionNorwegian() {
        return this.descriptionNorwegian;
    }

    public void setDescriptionNorwegian(String descriptionNorwegian) {
        this.descriptionNorwegian = descriptionNorwegian;
    }

    public int getNotificationId() {
        return this.notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

}
