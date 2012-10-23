package com.underdusken.kulturekalendar.json;

import android.util.Log;
import com.underdusken.kulturekalendar.data.EventsItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 10/10/12
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 * return null if something wrong
 */
public class JsonParseEvents {
    static public List<EventsItem> parse(String input){
        if(input==null)
            return null;

        List<EventsItem> eventsList = new ArrayList<EventsItem>();

        try {
            JSONObject object = new JSONObject(input);

            JSONArray eventsArray = null;
            try {
                eventsArray = object.getJSONArray("events");
            } catch (Exception e) {
                return null;
            }

            for (int i = 0; i < eventsArray.length(); i++) {
                EventsItem eventsItem = new EventsItem();
                JSONObject eventObject = eventsArray.getJSONObject(i);

                try{
                    eventsItem.setEventsId(eventObject.getString("id"));
                }catch (Exception e){
                    eventsItem.setEventsId("0");
                }
                try{
                    eventsItem.setName(eventObject.getString("name"));
                }catch (Exception e){
                    eventsItem.setName("");
                }
                try{
                    eventsItem.setType(eventObject.getString("type"));
                }catch (Exception e){
                    eventsItem.setType("");
                }
                try{
                    eventsItem.setAddress(eventObject.getString("address"));
                }catch (Exception e){
                    eventsItem.setAddress("");
                }
                try{
                    eventsItem.setGeoLatitude((float)eventObject.getDouble("geo_latitude"));
                }catch (Exception e){
                    eventsItem.setGeoLatitude(0.0f);
                }
                try{
                    eventsItem.setGeoLongitude((float)eventObject.getDouble("geo_longitude"));
                }catch (Exception e){
                    eventsItem.setGeoLongitude(0.0f);
                }
                try{
                    eventsItem.setDateStart(eventObject.getString("date_start"));
                }catch (Exception e){
                    eventsItem.setDateStart("");
                }
                try{
                    eventsItem.setDateEnd(eventObject.getString("date_end"));
                }catch (Exception e){
                    eventsItem.setDateEnd("");
                }
                try{
                    eventsItem.setPrice((float)eventObject.getDouble("price"));
                }catch (Exception e){
                    eventsItem.setPrice(0.0f);
                }
                try{
                    eventsItem.setAgeLimit((int)eventObject.getInt("ageLimit"));
                }catch (Exception e){
                    eventsItem.setAgeLimit(0);
                }
                try{
                    eventsItem.setBeerPrice((float)eventObject.getDouble("beerPrice"));
                }catch (Exception e){
                    eventsItem.setBeerPrice(0.0f);
                }
                try{
                    eventsItem.setDescriptionEnglish(eventObject.getString("descriptionEnglish"));
                }catch (Exception e){
                    eventsItem.setDescriptionEnglish("");
                }
                try{
                    eventsItem.setDescriptionNorwegian(eventObject.getString("descriptionNorwegian"));
                }catch (Exception e){
                    eventsItem.setDescriptionNorwegian("");
                }
                try{
                    eventsItem.setPicture(eventObject.getString("picture"));
                }catch (Exception e){
                    eventsItem.setPicture("");
                }
                try{
                    eventsItem.setSmallPicture(eventObject.getString("smallPicture"));
                }catch (Exception e){
                    eventsItem.setSmallPicture("");
                }
                try{
                    eventsItem.setWeekendRecommendationEnglish(eventObject.getString("weekendRecommendationEnglish"));
                }catch (Exception e){
                    eventsItem.setWeekendRecommendationEnglish("");
                }
                try{
                    eventsItem.setWeekendRecommendationNorwegian(eventObject.getString("weekendRecommendationNorwegian"));
                }catch (Exception e){
                    eventsItem.setWeekendRecommendationNorwegian("");
                }

                eventsList.add(eventsItem);
            }
            return eventsList;

        } catch (JSONException e) {
            Log.i("kultkal", e.getMessage());
            return null;
        }



    }
}
