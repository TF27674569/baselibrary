package com.demo.config;

import com.demo.R;
import com.demo.bean.FoodModel;
import com.demo.bean.HomeData;

import java.util.ArrayList;
import java.util.List;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/6
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class DataFactory {

    public static List<FoodModel> factoryFoods() {
        List<FoodModel> datas = new ArrayList<>();
        datas.add(new FoodModel("头发1", R.drawable.imag1, 58));
        datas.add(new FoodModel("头发2", R.drawable.imag2, 69));
        datas.add(new FoodModel("头发3", R.drawable.imag3,128));
        datas.add(new FoodModel("头发4", R.drawable.imag4, 665));
        datas.add(new FoodModel("头发5", R.drawable.imag5, 1088));
        datas.add(new FoodModel("头发6", R.drawable.imag6, 488));
        datas.add(new FoodModel("头发7", R.drawable.imag7, 88));
        datas.add(new FoodModel("头发8", R.drawable.imag8, 68));
        datas.add(new FoodModel("头发9", R.drawable.imag9, 129));
        datas.add(new FoodModel("头发10", R.drawable.imag10, 768));
        datas.add(new FoodModel("头发11", R.drawable.imag11, 352));
        datas.add(new FoodModel("头发12", R.drawable.imag12, 399));
        datas.add(new FoodModel("头发13", R.drawable.imag13, 699));
        return datas;
    }


    public static List<HomeData> getHomeData(){
        List<HomeData> datas = new ArrayList<>();
        datas.add(new HomeData("girl1", R.drawable.imag1, 60,40));
        datas.add(new HomeData("girl2", R.drawable.imag2, 40,60));
        datas.add(new HomeData("girl3", R.drawable.imag3, 60,40));
        datas.add(new HomeData("girl4", R.drawable.imag4, 40,60));
        datas.add(new HomeData("girl5", R.drawable.imag5, 60,40));
        datas.add(new HomeData("girl6", R.drawable.imag6, 40,60));
        datas.add(new HomeData("girl7", R.drawable.imag7, 60,40));
        datas.add(new HomeData("girl8", R.drawable.imag8, 40,60));
        datas.add(new HomeData("girl9", R.drawable.imag9, 60,40));
        datas.add(new HomeData("girl10", R.drawable.imag10, 40,60));
        datas.add(new HomeData("girl11", R.drawable.imag11, 40,60));
        datas.add(new HomeData("girl12", R.drawable.imag12, 40,60));
        datas.add(new HomeData("girl13", R.drawable.imag13, 40,60));
        return datas;
    }

}
