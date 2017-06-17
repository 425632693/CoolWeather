package com.example.administrator.coolweather.gson;

/**
 * Created by Administrator on 2017/6/16.
 */

import com.google.gson.annotations.SerializedName;

/**
 * 当天天气
 */
public class Now {

    @SerializedName("tmp")
    public String temperature;//天气温度

    @SerializedName("cond")
    public More more;//天气情况

    public class  More{
        @SerializedName("txt")
        public String info;
    }

    /**
     * json数据的格式是:
     * "now" : {
     *     "tmp" : "29",
     *     "cond" : {
     *         "txt" : "阵雨"
     *     }
     * }
     *
     *
     *
     */

}
