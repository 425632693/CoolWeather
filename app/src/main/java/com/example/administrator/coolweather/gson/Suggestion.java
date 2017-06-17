package com.example.administrator.coolweather.gson;

/**
 * Created by Administrator on 2017/6/16.
 */

import com.google.gson.annotations.SerializedName;

/**
 * 生活提示
 */
public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;//天气的舒适度

    @SerializedName("cw")
    public CarWash carWash;//洗车提示

    public Sport sport;//外出提示

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }


    /**
     * JSON数据的格式:
     * "suggestion":{
     *     "comf":{
     *         "txt":"提示的内容"
     *     },
     *     "cw":{
     *         "txt":"提示的内容"
     *     },
     *     "sport":{
     *         "txt":"提示的内容"
     *     }
     * }
     *
     *
     */


}
