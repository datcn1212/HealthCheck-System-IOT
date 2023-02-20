package com.example.iotproject.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class User {
    public static String USER_TOKEN;
    private String username = "";
    private String name = "";
    private int age = 0;
    private int weight = 0;
    private int height = 0;
    private List<Device> devicesList = new LinkedList<>();
    private static User shared;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setDevicesList(List<Device> devicesList) {
        this.devicesList = devicesList;
    }

    public List<Device> getDevicesList() {
        return devicesList;
    }

    // Only 1 user
    public static User getInstance() {
        if (shared == null) {
            shared = new User();
        }
        return shared;
    }

    public void getData(JSONObject userInfo) throws JSONException {

        setUsername(userInfo.getString("username"));
        if (userInfo.has("name"))
            setName(userInfo.getString("name"));
        if (userInfo.has("age"))
            setAge(Integer.parseInt(userInfo.getString("age")));
        if (userInfo.has("weight"))
            setWeight(Integer.parseInt(userInfo.getString("weight")));
        if (userInfo.has("height"))
            setHeight(Integer.parseInt(userInfo.getString("height")));

    }

    public void reset() {
        age = 0;
        height = 0;
        weight = 0;
        devicesList = new ArrayList<>();
        name = "";
        username = "";
    }
}
