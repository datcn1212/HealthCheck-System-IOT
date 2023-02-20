package com.example.iotproject.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceData {
    private int heartRate;
    private int bodyTemperature;
    private int systolic;
    private int diastolic;
    private String create;
    private String warning;

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(int bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public void getData(JSONObject obj) throws JSONException {
        if (obj.has("heartRate"))
            setHeartRate(obj.getInt("heartRate"));
        if (obj.has("bodyTemperature"))
            setBodyTemperature(obj.getInt("bodyTemperature"));
        if (obj.has("systolic"))
            setSystolic(obj.getInt("systolic"));
        if (obj.has("diastolic"))
            setDiastolic(obj.getInt("diastolic"));
        if (obj.has("create"))
            setCreate(obj.getString("create"));
        if (obj.has("warning"))
            setWarning(obj.getString("warning"));
    }
}
