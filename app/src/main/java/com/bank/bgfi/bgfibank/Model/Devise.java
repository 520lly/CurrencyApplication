package com.bank.bgfi.bgfibank.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ayach on 7/13/16.
 */
public class Devise  implements Parcelable{

    private  String namedevice ;
    private int stat ;
    private double buyPrice;
    private double selPrice;
    private double spread;

    public Devise () {}
    public Devise (Parcel in) {

        this.namedevice = in.readString();
        this.stat = in.readInt();
        this.buyPrice = in.readDouble();
        this.selPrice = in.readDouble();
        this.spread = in.readDouble();


    }
    public Devise(String namedevice, int stat, double buyPrice, double selPrice,double spread) {
        this.namedevice = namedevice;
        this.stat = stat;
        this.buyPrice = buyPrice;
        this.selPrice = selPrice;
        this.spread=spread;
    }

    public String getNamedevice() {
        return namedevice;
    }

    public void setNamedevice(String namedevice) {
        this.namedevice = namedevice;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSelPrice() {
        return selPrice;
    }

    public void setSelPrice(double selPrice) {
        this.selPrice = selPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
     dest.writeString(getNamedevice());
        dest.writeInt(getStat());
        dest.writeDouble(getBuyPrice());
        dest.writeDouble(getSelPrice());
        dest.writeDouble(getSpread());
    }

    public static final Parcelable.Creator<Devise> CREATOR = new Parcelable.Creator<Devise>()
    {
        @Override
        public Devise createFromParcel(Parcel source)
        {
            return new Devise(source);
        }

        @Override
        public Devise[] newArray(int size)
        {
            return new Devise[size];
        }
    };





}
