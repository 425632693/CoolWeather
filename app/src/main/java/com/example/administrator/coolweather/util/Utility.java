package com.example.administrator.coolweather.util;

/**
 * Created by Administrator on 2017/6/15.
 */

import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.coolweather.db.City;
import com.example.administrator.coolweather.db.County;
import com.example.administrator.coolweather.db.Province;
import com.example.administrator.coolweather.gson.Forecast;
import com.example.administrator.coolweather.gson.Weather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 解析和处理服务器返回来的省市县的数据
 */
public class Utility {

    private static final String TAG = "Utility";

    //获取服务器返回到省的响应数据
    public static boolean handleProvinceResponse(String response){
        //判断服务器是否返回响应数据，如果返回来了就把数据解析并赋值到实体类
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvince = new JSONArray(response);
                for(int i = 0;i<allProvince.length();i++){
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceCode(provinceObject.getInt("id"));
                    Log.d(TAG, "解析的json数据的省的id是"+provinceObject.getInt("id"));
                    province.setProvinceName(provinceObject.getString("name"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //获取服务器返回的市的响应数据
    public static boolean hendleCityResponse(String response,int provinceId) {
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCity = new JSONArray(response);
                for(int i = 0;i<allCity.length();i++){
                    JSONObject cityObject = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(cityObject.getInt("id"));
                    city.setCityName(cityObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    //获取服务器返回的县的响应的数据
    public static boolean hendleCountyResponse(String response,int cityId) {
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounty = new JSONArray(response);
                for(int i = 0;i<allCounty.length();i++){
                    JSONObject countyObject = allCounty.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 将返回的JSON数据解析成Weather实体类
     * @param response
     * @return
     */
    public static Weather handleWeatherResponse(String response){
        try {
            //获取服务器响应回来的数据
            JSONObject jsonObject = new JSONObject(response);
            //把数据存到数组中
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            //把数组中的json数据转换成String字符串
            String weatherContent = jsonArray.getJSONObject(0).toString();

            Log.d(TAG, "json数据的字符串是:======="+weatherContent);
            //使用Gson解析数据并赋值到Weather实体类里
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static Weather handleWeatherResponse(String response){
////        Gson gson = new Gson();
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
//            for(int i = 0;i<jsonArray.length();i++){
//                JSONObject json = jsonArray.getJSONObject(i);
//                Weather weather = new Weather();
//                Log.d(TAG, "json里的数据是:-------"+json);
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}
