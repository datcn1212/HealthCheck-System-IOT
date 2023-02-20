package com.example.iotproject.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Device {
    private String _id;
    private String Id;
    private String name;
    private String createTime;
    private String info;
    private ArrayList<DeviceData> datas;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public ArrayList<DeviceData> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<DeviceData> datas) {
        this.datas = datas;
    }

    public void getData(JSONObject obj) throws JSONException {

        this.set_id(obj.getString("_id"));
        this.setId(obj.getString("Id"));
        this.setName(obj.getString("name"));
        this.setCreateTime(obj.getString("create"));
        if (obj.has("info"))
            this.setInfo(obj.getString("info"));
        else
            this.setInfo("");
    }

    public void getHumanData(JSONArray arr) throws JSONException {
        datas = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            DeviceData data = new DeviceData();
            data.getData(obj);
            datas.add(data);
        }
//        for (int i = 0; i <= 20; i++) {
//            DeviceData data = new DeviceData();
//            data.setHeartRate(40);
//            data.setSystolic(50);
//            data.setDiastolic(60);
//            data.setBodyTemperature(30);
//            data.setCreate("2022-24-07 02:04");
//            datas.add(data);
//        }

    }
}
