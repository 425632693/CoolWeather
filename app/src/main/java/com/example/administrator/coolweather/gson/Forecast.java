package com.example.administrator.coolweather.gson;

/**
 * Created by Administrator on 2017/6/16.
 */

import com.google.gson.annotations.SerializedName;

/**
 * 未来一天的天气信息
 */
public class Forecast {

    public String date;//天气日期

    @SerializedName("tmp")
    public Temperature temperature;//天气温度

    @SerializedName("cond")
    public More more;//天气情况


    public class Temperature{
        public String max;//天气最高温度

        public String min;//天气最低温度
    }

    public class More{
        @SerializedName("txt_d")
        public String info;//天气注意事项
    }


    /**
     * JSON数据的格式:
     * "daily_forecast":[
     *      //每天的天气
     *      {
     *        "date":"2017-06-16",
     *        "cond":{
     *            "txt_d":"阵雨"
     *        },
     *        "tmp":{
     *            "max":"34",
     *            "min":"27"
     *        }
     *      },
     *
     *      //每天的天气
     *      {
     *          "date":"2017-06-17",
     *          "cond":{
     *              "txt_d":"多云"
     *          },
     *          "tmp":{
     *              "max":"35"
     *              "min":"29"
     *          }
     *      },
     *
     *      //每天的天气
     *
     *      ....
     *
     * ]
     *
     *
     */


}
