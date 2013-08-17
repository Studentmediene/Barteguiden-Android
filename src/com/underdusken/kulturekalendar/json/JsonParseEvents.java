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
    static public List<EventsItem> parse(String input) {
        if (input == null)
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

                try {
                    eventsItem.setEventsId(eventObject.getString("eventID"));
                } catch (Exception e) {
                    eventsItem.setEventsId("0");
                }
                try {
                    eventsItem.setTitle(eventObject.getString("title"));
                } catch (Exception e) {
                    eventsItem.setTitle("");
                }
                try {
                    eventsItem.setCategoryID(eventObject.getString("categoryID"));
                } catch (Exception e) {
                    eventsItem.setCategoryID("");
                }
                try {
                    eventsItem.setAddress(eventObject.getString("address"));
                } catch (Exception e) {
                    eventsItem.setAddress("");
                }
                try {
                    eventsItem.setGeoLatitude((float) eventObject.getDouble("latitude"));
                } catch (Exception e) {
                    eventsItem.setGeoLatitude(0.0f);
                }
                try {
                    eventsItem.setGeoLongitude((float) eventObject.getDouble("longitude"));
                } catch (Exception e) {
                    eventsItem.setGeoLongitude(0.0f);
                }
                try {
                    eventsItem.setDateStart(eventObject.getString("startAt"));
                } catch (Exception e) {
                    eventsItem.setDateStart("");
                }

                try {
                    eventsItem.setPrice(eventObject.getInt("price"));
                } catch (Exception e) {
                    eventsItem.setPrice(0);
                }
                try {
                    eventsItem.setAgeLimit(eventObject.getInt("ageLimit"));
                } catch (Exception e) {
                    eventsItem.setAgeLimit(0);
                }
                /*
                try {
                    eventsItem.setBeerPrice((float) eventObject.getDouble("beerPrice"));
                } catch (Exception e) {
                    eventsItem.setBeerPrice(0.0f);
                }*/


                JSONArray descriptionArray = null;
                try {
                    descriptionArray = eventObject.getJSONArray("descriptions");
                } catch (Exception e) {
                    eventsItem.setDescriptionEnglish("");
                    eventsItem.setDescriptionNorwegian("");
                }

                if (descriptionArray != null){
                    for (int j = 0; j < descriptionArray.length(); j++) {
                        JSONObject descriptionObject = descriptionArray.getJSONObject(j);

                        String language = "";
                        try {
                            language = descriptionObject.getString("language");
                        } catch (Exception e) {
                        }
                        String text = "";
                        try {
                            text = descriptionObject.getString("text");
                        } catch (Exception e) {
                        }

                        if (language.equals("en")){
                            eventsItem.setDescriptionEnglish(text);
                        }else if(language.equals("nb")){
                            eventsItem.setDescriptionNorwegian(text);
                        }
                    }
                }
                try{
                    eventsItem.setPlaceName(eventObject.getString("placeName"));
                }catch(Exception e){
                    eventsItem.setPlaceName("");
                }

                try{
                    eventsItem.setisRecomended(eventObject.getBoolean("isRecommended"));
                }catch(Exception e){
                    eventsItem.setisRecomended(false);
                }

                try{
                    eventsItem.setImageURL(eventObject.getString("imageURL"));
                }catch (Exception e){
                    eventsItem.setImageURL("");
                }

                try{
                    eventsItem.setEventURL(eventObject.getString("eventURL"));
                }catch(Exception e){
                    eventsItem.setEventURL("");
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

