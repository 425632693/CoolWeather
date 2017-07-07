package com.example.administrator.coolweather.title;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.administrator.coolweather.R;
import com.example.administrator.coolweather.WeatherActivity;
import com.example.administrator.coolweather.util.MyAppliction;

/**
 * Created by Administrator on 2017/6/21.
 */

/**
 * 显示menu菜单
 */
public class Title_Button implements View.OnClickListener{
    private static final String TAG = "Title_Button";
    private final int tianjia= 1;
    private final int shezhi = 2;
    private Handler handler = new Handler();
    private Button addChengshi,setting;


    public void ShowPopupMenu(View view){
        Log.d(TAG, "进入PopupWindow");
        //准备PopupWindow布局
        View popupView = LayoutInflater.from(MyAppliction.getContext()).inflate(R.layout.title_button,null,false);
        //初始化一个PopupWindow,width和height都是wrap_content
        final PopupWindow popupWindow=new PopupWindow(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //设置PopupWindow的视图内容
        popupWindow.setContentView(popupView);
        //点击空白区域PopupWindow消失,这里必须先设置setBackgroundDrawable,否则点击无反应
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);//必须写
        //设置PopupWindow动画
        popupWindow.setAnimationStyle(R.style.AnimDown);
        //设置是否允许PopupWindow的范围超过屏幕范围
        popupWindow.setClippingEnabled(true);

        //根据手指按下的状态来关闭弹框
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        //设置弹框显示在父布局里
        popupWindow.showAsDropDown(view);


        addChengshi = (Button) popupView.findViewById(R.id.addCity);
        setting = (Button) popupView.findViewById(R.id.Settings);
        addChengshi.setOnClickListener(this);
        setting.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //城市管理
            case R.id.addCity:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "进入到城市管理的Message发送线程里了");
                        Message message = new Message();
                        message.what = tianjia;
                        handler.sendMessage(message);
                        Log.d(TAG, "发送的Message值是:"+message);
                    }
                }).start();
                break;
            //设置
            case R.id.Settings:

                break;
            default:
                break;
        }
    }


//    public void ShowPopupMenu(final Context context, View view){
//        //View 当前 PopupMenu 显示的相对 View 的位置
//        PopupMenu popupMenu = new PopupMenu(context,view);
////        popupMenu.getMenu().add(Menu.FIRST+1);
//        //Menu布局
//        popupMenu.getMenuInflater().inflate(R.menu.menumain,popupMenu.getMenu());
//
//        //menu的点击事件
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Log.d(TAG, "当前点击的菜单id是:"+item.getItemId());
//                switch (item.getItemId()){
//                    //城市设置
//                    case 2131427464:
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Message message = new Message();
//                                message.what = addActivity;
//                                handler.sendMessage(message);
//                            }
//                        }).start();
//                        break;
//                    //设置
//                    case 2131427465:
//
//                        break;
//                    default:
//                        break;
//                }
//
//                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
//
//        //关闭menu菜单
//        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//            @Override
//            public void onDismiss(PopupMenu menu) {
//                Toast.makeText(context, "关闭menu", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //显示menu菜单
//        popupMenu.show();
//    }


}
