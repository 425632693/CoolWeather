package com.example.administrator.coolweather.util;

/**
 * Created by Administrator on 2017/6/20.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * 判断天气情况显示天气图标
 */
public class WeatherPicUtil {

    private static WeatherPicUtil wpu = null;
    private static SharedPreferences.Editor editor;
    private String weatherPic = null;

    private WeatherPicUtil(){};

    public static WeatherPicUtil getInstatic(){
        if(wpu == null){
            wpu = new WeatherPicUtil();
        }
        return wpu;
    }


    public String getWeather(String weatherInfo,Context context){
        editor =  PreferenceManager.getDefaultSharedPreferences(context).edit();
        if(weatherInfo.equals("晴")){
            weatherPic = "https://cdn.heweather.com/cond_icon/100.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("多云")||weatherInfo.equals("少云")||weatherInfo.equals("晴间多云")){
            weatherPic = "https://cdn.heweather.com/cond_icon/101.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("阴")){
            weatherPic = "https://cdn.heweather.com/cond_icon/104.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("平静")||weatherInfo.equals("有风")||weatherInfo.equals("微风")
                ||weatherInfo.equals("和风")||weatherInfo.equals("清风")){
            weatherPic = "https://cdn.heweather.com/cond_icon/200.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("大风")||weatherInfo.equals("强风")||weatherInfo.equals("疾风")
                ||weatherInfo.equals("烈风")){
            weatherPic = "https://cdn.heweather.com/cond_icon/207.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("阵雨")||weatherInfo.equals("强阵雨")||weatherInfo.equals("强阵雨")
                ||weatherInfo.equals("强雷阵雨")){
            weatherPic = "https://cdn.heweather.com/cond_icon/301.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("小雨")||weatherInfo.equals("中雨")||weatherInfo.equals("大雨")
                ||weatherInfo.equals("暴雨")){
            weatherPic = "https://cdn.heweather.com/cond_icon/306.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("小雪")||weatherInfo.equals("中雪")||weatherInfo.equals("大雪")
                ||weatherInfo.equals("暴雪")){
            weatherPic = "https://cdn.heweather.com/cond_icon/401.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("雨夹雪")||weatherInfo.equals("雨雪天气")||weatherInfo.equals("阵雨夹雪")
                ||weatherInfo.equals("阵雪")){
            weatherPic = "https://cdn.heweather.com/cond_icon/405.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("雾")){
            weatherPic = "https://cdn.heweather.com/cond_icon/501.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("霾")){
            weatherPic = "https://cdn.heweather.com/cond_icon/502.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("扬沙")){
            weatherPic = "https://cdn.heweather.com/cond_icon/503.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else if(weatherInfo.equals("浮尘")){
            weatherPic = "https://cdn.heweather.com/cond_icon/504.png";
            editor.putString("weatherImg",weatherPic);
            editor.apply();
        }else{
            editor.remove("weatherImg");
            Toast.makeText(context, "获取天气图标失败", Toast.LENGTH_SHORT).show();
        }
        return weatherPic;
    }



}
