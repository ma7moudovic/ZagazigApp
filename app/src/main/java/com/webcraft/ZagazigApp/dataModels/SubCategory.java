package com.webcraft.ZagazigApp.dataModels;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by T on 5/29/2016.
 */
public class SubCategory {

    String id , description ;
    JSONObject jsonObject ;

    public SubCategory() {
    }

    public SubCategory(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public SubCategory(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        try {
            this.id =jsonObject.getString("id");
            this.description=jsonObject.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
