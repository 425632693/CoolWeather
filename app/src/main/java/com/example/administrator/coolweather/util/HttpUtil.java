package com.example.administrator.coolweather.util;

/**
 * Created by Administrator on 2017/6/15.
 */

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 和服务器进行交互的okhttp工具类
 */
public class HttpUtil {
    //创建一个访问服务器的静态方法(两个参数分别是：1.请求的地址    2.注册一个处理服务器响应的回调)
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        //创建一个发送请求的对象
        OkHttpClient client=new OkHttpClient();
        //创建一个请求对象
        Request request = new Request.Builder().url(address).build();
        //发送请求
        client.newCall(request).enqueue(callback);
    }


}
