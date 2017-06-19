package com.example.administrator.coolweather.gson;

/**
 * Created by Administrator on 2017/6/16.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 总的实体类(引用别的实体类)
 */
public class Weather {

    //天气数据中包含的status数据,成功返回ok,失败返回具体原因
    public String status;

    //城市信息
    public Basic basic;

    //空气质量
    public AQI aqi;

    //当天天气
    public Now now;

    //生活提示
    public Suggestion suggestion;

    //未来几天的信息(数组)
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
