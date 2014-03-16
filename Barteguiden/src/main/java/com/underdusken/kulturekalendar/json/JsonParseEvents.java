package com.underdusken.kulturekalendar.json;

import android.util.Log;

import com.underdusken.kulturekalendar.data.EventItem;

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
    static public List<EventItem> parse(String input) {
        if (input == null) return null;

        List<EventItem> eventsList = new ArrayList<EventItem>();

        try {
            JSONObject object = new JSONObject(input);

            JSONArray eventsArray = null;
            try {
                eventsArray = object.getJSONArray("events");
            } catch (Exception e) {
                return null;
            }

            for (int i = 0; i < eventsArray.length(); i++) {
                EventItem eventItem = new EventItem();
                JSONObject eventObject = eventsArray.getJSONObject(i);

                try {
                    eventItem.setEventsId(eventObject.getString("eventID"));
                } catch (Exception e) {
                    eventItem.setEventsId("0");
                }
                try {
                    eventItem.setTitle(eventObject.getString("title"));
                } catch (Exception e) {
                    eventItem.setTitle("");
                }
                try {
                    eventItem.setCategoryID(eventObject.getString("categoryID"));
                } catch (Exception e) {
                    eventItem.setCategoryID("");
                }
                try {
                    eventItem.setAddress(eventObject.getString("address"));
                } catch (Exception e) {
                    eventItem.setAddress("");
                }
                try {
                    eventItem.setGeoLatitude((float) eventObject.getDouble("latitude"));
                    eventItem.setIsGeo(true);
                } catch (Exception e) {
                    eventItem.setGeoLatitude(200.0f);
                }
                try {
                    eventItem.setGeoLongitude((float) eventObject.getDouble("longitude"));
                } catch (Exception e) {
                    eventItem.setGeoLongitude(200.0f);
                }
                try {
                    eventItem.setDateStart(eventObject.getString("startAt"));
                } catch (Exception e) {
                    eventItem.setDateStart("");
                }

                try {
                    eventItem.setPrice(eventObject.getInt("price"));
                } catch (Exception e) {
                    eventItem.setPrice(0);
                }
                try {
                    eventItem.setAgeLimit(eventObject.getInt("ageLimit"));
                } catch (Exception e) {
                    eventItem.setAgeLimit(0);
                }
                /*
                try {
                    eventItem.setBeerPrice((float) eventObject.getDouble("beerPrice"));
                } catch (Exception e) {
                    eventItem.setBeerPrice(0.0f);
                }*/


                JSONArray descriptionArray = null;
                try {
                    descriptionArray = eventObject.getJSONArray("descriptions");
                } catch (Exception e) {
                    eventItem.setDescriptionEnglish("");
                    eventItem.setDescriptionNorwegian("");
                }

                if (descriptionArray != null) {
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

                        if (language.equals("en")) {
                            eventItem.setDescriptionEnglish(text);
                        } else if (language.equals("nb")) {
                            eventItem.setDescriptionNorwegian(text);
                        }
                    }
                }
                try {
                    eventItem.setPlaceName(eventObject.getString("placeName"));
                } catch (Exception e) {
                    eventItem.setPlaceName("");
                }

                try {
                    eventItem.setisRecomended(eventObject.getBoolean("isRecommended"));
                } catch (Exception e) {
                    eventItem.setisRecomended(false);
                }

                try {
                    eventItem.setImageURL(eventObject.getString("imageURL"));
                } catch (Exception e) {
                    eventItem.setImageURL("");
                }

                try {
                    eventItem.setEventURL(eventObject.getString("eventURL"));
                } catch (Exception e) {
                    eventItem.setEventURL("");
                }

                eventsList.add(eventItem);
            }
            return eventsList;

        } catch (JSONException e) {
            Log.i("kultkal", e.getMessage());
            return null;
        }
    }


}

