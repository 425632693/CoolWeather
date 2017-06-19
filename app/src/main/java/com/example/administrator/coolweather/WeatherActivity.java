package com.example.administrator.coolweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.coolweather.gson.Forecast;
import com.example.administrator.coolweather.gson.Weather;
import com.example.administrator.coolweather.util.HttpUtil;
import com.example.administrator.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";

    private ScrollView weatherLayout;
    private TextView titleCity,titleUpdateTime,degreeText,weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView apiText,pm25Text,comfortText,carWashText,sportText;
    private ImageView bingPicImg;
    private SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //背景图和状态栏融合在一起(只有Android5.0以后才有,所以判断版本号)
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);
        //控件初始化
        initView();
        //判断天气信息有无缓存
        query_weather();

    }


    //初始化控件
    private void initView(){
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        apiText = (TextView) findViewById(R.id.api_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
    }

    /**
     * 判断有无天气的缓存数据
     */
    private void query_weather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //先在SharedPreferences缓存里加载图片,如果没有就调用loadBingPic()方法进行网络请求加载图片
        String bingPic = prefs.getString("bing_pic",null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }

        String weatherString = prefs.getString("weather",null);
        if(weatherString!=null){
            //有缓存时直接解析获取天气数据,并调用showWeatherInfo()方法处理并显示数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{
            //无缓存时去服务器查询天气
            //根据JSON数据中的weather_id获取天气情况的数据id
//            String weatherId = getIntent().getStringExtra("weather_id");

            mWeatherId = getIntent().getStringExtra("weather_id");

            weatherLayout.setVisibility(View.INVISIBLE);
            //根据weatherId去服务器上进行数据查询
            requestWeather(mWeatherId);
//            requestWeather(weatherId);
        }

        //刷新的时候调用服务器请求的方法
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

    }

    /**
     * 根据天气id去服务器请求城市天气信息
     * @param weatherId
     * 完整的请求路径是:
     * http://guolin.tech/api/weather?cityid=CN101190401&key=10b04897278049f8abacea77aa16ae29
     *
     */
    public void requestWeather(String weatherId){
        //使用天气情况的id和key值拼接一个上面的请求路径
        String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=10b04897278049f8abacea77aa16ae29";
        Log.d(TAG, "请求的路径是:"+weatherUrl);
        //向路径发送请求
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            //获取响应失败
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }

                });
            }

            //获取响应成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取服务器响应的数据
                final String responseText = response.body().string();

                Log.d(TAG, "获取到的响应的数据是:"+responseText);
                //使用服务器响应的数据进行获取JSON数据存入Weather实体类
                final Weather weather = Utility.handleWeatherResponse(responseText);
                Log.d(TAG, "存储到Weather实体类中的天气数据长度是:"+weather.forecastList.size());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //判断实体类对象不等于空并且JSON数据里的status是否等于"ok"
                        if(weather!=null && "ok".equals(weather.status)){
                            //创建一个SharedPreferences存储对象
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
                            //将数据缓存到SharedPreferences中
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        //结束刷新
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }


    /**
     * 处理并展示数据到页面上
     * @param weather
     */
    private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.cityName;//获取城市名
        String updateTime = weather.basic.update.updateTime.split(" ")[1];//获取天气更新时间
        String degree = weather.now.temperature+"℃";//获取天气温度
        String weatherInfo = weather.now.more.info;//获取天气情况
        //把天气的信息赋值到控件中
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        /**
         * removeAllViews() 也调用了removeAllViewsInLayout(),
         * 但是后面还调用了requestLayout(),这个方法是当View的布局发生改变会调用它来更新当前视图,
         *  移除子View会更加彻底
         */
        forecastLayout.removeAllViews();

        //把获取的天气数据保存到未来几天的天气信息的实体类里
        for(Forecast forecast:weather.forecastList){
            //生成一个布局
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            //添加自己生成的布局
            forecastLayout.addView(view);
        }

        //显示api
        if(weather.aqi!=null){
//            apiText.setText(weather.aqi.aqiCity.api);
//            pm25Text.setText(weather.aqi.aqiCity.pm25);
        }

        //显示生活提示
        String comfort = "舒适度:\n"+weather.suggestion.comfort.info;
        String carWash = "洗车指数:\n"+weather.suggestion.carWash.info;
        String sport = "运动指数:\n"+weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        //显示布局信息
        weatherLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "加载必应每日一图");
                //获取请求的响应
                final String bingPic = response.body().string();
                /**
                 * 创建一个SharedPreferences缓存,把获取到的图片存放到缓存中,并切换回到主线程使用Glide加载图片
                 * 这样下次进入应用的时候不要重新发送图片请求了
                 */
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }



}








