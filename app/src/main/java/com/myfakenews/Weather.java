package com.myfakenews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Weather {
    @SerializedName("main")
    @Expose
    private HashMap<String, Object> mainObj;

    public HashMap<String, Object> getMainObj() {
        return mainObj;
    }

    public void setMainObj(HashMap<String, Object> mainObj) {
        this.mainObj = mainObj;
    }

    public String getTemp(){
        return mainObj.get("temp").toString();
    }
    public String getMinTemp(){
        return mainObj.get("temp_min").toString();
    }
    public String getMaxTemp(){
        return mainObj.get("temp_max").toString();
    }
}
