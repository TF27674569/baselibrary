package com.demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/6
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class FoodModel implements Parcelable {


    private String name;

    private int path;

    private int money;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public FoodModel(String name, int path, int money) {
        this.name = name;
        this.path = path;
        this.money = money;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.path);
        dest.writeInt(this.money);
    }

    public FoodModel() {
    }

    protected FoodModel(Parcel in) {
        this.name = in.readString();
        this.path = in.readInt();
        this.money = in.readInt();
    }

    public static final Creator<FoodModel> CREATOR = new Creator<FoodModel>() {
        @Override
        public FoodModel createFromParcel(Parcel source) {
            return new FoodModel(source);
        }

        @Override
        public FoodModel[] newArray(int size) {
            return new FoodModel[size];
        }
    };
}
