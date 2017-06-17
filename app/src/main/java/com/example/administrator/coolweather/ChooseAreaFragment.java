package com.example.administrator.coolweather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.coolweather.db.City;
import com.example.administrator.coolweather.db.County;
import com.example.administrator.coolweather.db.Province;
import com.example.administrator.coolweather.util.HttpUtil;
import com.example.administrator.coolweather.util.Utility;

import org.json.JSONException;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/15.
 */

public class ChooseAreaFragment extends Fragment {
    private static final String TAG = "ChooseAreaFragment";

    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<County> countyList;

    /**
     * 选中的省
     */
    private Province selectProvince;

    /**
     * 选中的市
     */
    private City selectCity;

    /**
     * 当前选中的级别
     */
    private int currentLevel;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);

        adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,dataList);
        listView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断当前选中的城市的级别进行调用对应的方法进行查询并刷新页面显示的内容(从上一级到下一级)
                if(currentLevel == LEVEL_PROVINCE){
                    selectProvince = provinceList.get(position);
                    Log.d(TAG, "当前点击的省是："+selectProvince.getProvinceName()+"----"+selectProvince.getProvinceCode());
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectCity = cityList.get(position);
                    Log.d(TAG, "当前点击的市是："+selectCity.getCityName()+"======"+selectCity.getCityCode());
                    queryCounties();
                }
            }
        });

        //点击返回判断当前的城市等级来调用对应的方法刷新页面显示的内容(从下一级到上一级)
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    queryProvince();
                }
            }
        });
        queryProvince();
    }


    /**
     * 查询全国的省，优先从数据库进行查询，如果没有再去服务器上查询
     */
    private void queryProvince(){
        titleText.setText("中国");
        //由于现在就是省，最高的城市级别，不能再返回上一级了，所以把返回键隐藏
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province : provinceList){
                Log.d(TAG, "实体类里存储的省是："+province.getProvinceCode());
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    /**
     * 查询全国的市，优先从数据库进行查询，如果没有再去服务器上查询
     */
    private void queryCities(){
        titleText.setText(selectProvince.getProvinceName());
        //当前城市级别是市，有上一级城市可以返回，所以把返回键显示出来
        backButton.setVisibility(View.VISIBLE);
        //根据省的编号id进行查询
        cityList=DataSupport.where("provinceid=?", String.valueOf(selectProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            //获取当前选中的是哪个省
            int provinceCode = selectProvince.getProvinceCode();
            Log.d(TAG, "当前选中的省的编号是："+provinceCode);
            String address = "http://guolin.tech/api/china/"+provinceCode;
            Log.d(TAG, "当前请求的地址是"+address);
            queryFromServer(address,"city");
        }
    }

    private void queryCounties(){
        titleText.setText(selectCity.getCityName());
        //当前城市级别是县，有上一级城市可以返回，所以把返回键显示出来
        backButton.setVisibility(View.VISIBLE);
        //根据当前选中的市的id去进行所属县的查询
        countyList=DataSupport.where("cityId = ?", String.valueOf(selectCity.getId())).find(County.class);
        if(countyList.size()>0){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else{
            //获取当前选中省的编号
            int provinceCode = selectProvince.getProvinceCode();
            Log.d(TAG, "当前选中的省的编号是："+provinceCode);
            //获取当前选中市的编号
            int cityCode = selectCity.getCityCode();
            Log.d(TAG, "当前选中的市的编号是："+cityCode);
            //通过省市的编号组成一个地址
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            //根据传入的城市类型和地址进行服务器的查询
            queryFromServer(address,"county");
        }
    }



    //根据传入的地址和类型从服务器上查询省市县数据
    private void queryFromServer(String address,final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            //获取返回的响应失败
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread() 方法切换到主线程里进行业务逻辑处理
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭进度对话框
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //获取服务器返回来的响应的数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.d(TAG, "返回的response是："+responseText);
                boolean result = false;
                if("province".equals(type)){
                    Log.d(TAG, "获取JSON数据");

                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result = Utility.hendleCityResponse(responseText,selectProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.hendleCountyResponse(responseText,selectCity.getId());
                }

                //根据获取的结果的类型切换到对应的方法
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvince();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }

            }
        });


    }


    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }




}


