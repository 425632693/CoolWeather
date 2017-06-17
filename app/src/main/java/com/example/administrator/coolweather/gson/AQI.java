package com.example.administrator.coolweather.gson;

/**
 * Created by Administrator on 2017/6/16.
 */

/**
 * 空气质量
 */
public class AQI {

    public AQICity aqiCity;

    public class AQICity {

        public String api;//空气API指数

        public String pm25;//空气pm2.5指数

    }


/**
 * JSON数据的格式是:
 * "api" : {
 *      "city" : {
 *          "api" : "44"
 *          "pm25" : "13"
 *      }
 * }
 *
 */


}




