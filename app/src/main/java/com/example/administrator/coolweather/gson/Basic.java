package com.example.administrator.coolweather.gson;

/**
 * Created by Administrator on 2017/6/16.
 */

import com.google.gson.annotations.SerializedName;

/**
 * 天气的城市基础信息
 *
 * 由于JSON中的一些字段可能不适合直接作为Java字段命名，因此使用 @SerializedName(" ")注解的方式让
 * JSON字段和Java字段之间建立映射关系
 */
public class Basic {

    @SerializedName("city")
    public String cityName;//城市名

    @SerializedName("id")
    public String weatherId;//城市对应的天气的id

    public Update update;

    /**
     * loc 表示天气的更新时间
     */
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }


    /**
     * JSON数据的格式是：
     * "basic" : {
     *     "city" : "安阳",
     *     "id" : "CN101180201",
     *     "update" : {
     *         "loc" : "2017-06-17 17:30"
     *     }
     * }
     *
     *
     */

}
