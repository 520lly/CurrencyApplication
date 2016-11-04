package com.bank.bgfi.bgfibank.Adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bank.bgfi.bgfibank.R;
import com.bank.bgfi.bgfibank.Util.CurrencySql;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayach on 8/15/16.
 */
public class DeviseForAllAdapter extends ArrayAdapter {


    private List<String> devises = new ArrayList<>();
    private Context mcontext ;
    private int resourceId ;
    CurrencySql currencySql;

    public DeviseForAllAdapter(Context context, int resource, List<String> devises) {
        super(context, resource, devises);
        this.mcontext = context ;
        this.resourceId = resource;
        this.devises = devises;
    }

    public List<String> getDevises() {
        return devises;
    }

    public void setDevises(List<String> devises) {
        this.devises = devises;
    }

    public Context getMcontext() {
        return mcontext;
    }

    public void setMcontext(Context mcontext) {
        this.mcontext = mcontext;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public int getCount() {
        return devises.size();
    }

    @Override
    public String getItem(int position) {

        return devises.get(position);
    }

    static class AdapterHolderDevice {
        String devise;
        TextView nameDevise;
        TextView nameCamplite;

    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {


        View row = convertView;
        final AdapterHolderDevice holder;
        currencySql = new CurrencySql(mcontext);
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(getResourceId(), parent, false);
            holder = new AdapterHolderDevice();
            holder.nameDevise = (TextView) row.findViewById(R.id.dev_name_fav);
            holder.nameCamplite = (TextView) row.findViewById(R.id.dev_name_cp);
            row.setTag(holder);
        } else {

            holder = (AdapterHolderDevice) row.getTag();

        }
        String change = devises.get(position);
        String one = change.substring(0,3);
        String two = change.substring(4,7);



        holder.nameDevise.setText(devises.get(position));
        holder.nameCamplite.setText(getStringResourceByName(one)+" vs "+getStringResourceByName(two));


        return row;


    }
    private String getStringResourceByName(String aString) {
        String packageName = mcontext.getPackageName();
        int resId = mcontext.getResources().getIdentifier(aString, "string", packageName);
        return mcontext.getString(resId);
    }
}
