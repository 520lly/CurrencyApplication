package com.bank.bgfi.bgfibank.Adapter;

import android.animation.ValueAnimator;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bank.bgfi.bgfibank.Model.Devise;
import com.bank.bgfi.bgfibank.R;
import com.bank.bgfi.bgfibank.Util.CurrencySql;

import java.io.Console;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ayach on 7/13/16.
 */
public class DeviceAdapter extends ArrayAdapter <Devise>{

 private   ArrayList <Devise> devices = new ArrayList<Devise>();
    private Context mcontext ;
    private int resourceId ;
    CurrencySql currencySql;
    public DeviceAdapter(Context context, int resource, ArrayList <Devise> devices) {
        super(context, resource, devices);
        this.mcontext = context ;
        this.resourceId = resource;
        this.devices = devices;
    }

    public ArrayList<Devise> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Devise> devices) {
        this.devices = devices;
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
        return devices.size();
    }

    @Override
    public Devise getItem(int position) {

        return devices.get(position);
    }

    static class AdapterHolderDevice {
        Devise device;
        TextView nameDevice;
        TextView buy;
        TextView sel;
        TextView date;
        TextView spead;

    }

    public void remove(int position) {
        devices.remove(position);
        notifyDataSetChanged();
    }




    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View row = convertView;
        currencySql = new CurrencySql(mcontext);
        final AdapterHolderDevice holder;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(getResourceId(), parent, false);
            holder = new AdapterHolderDevice();
            holder.nameDevice = (TextView) row.findViewById(R.id.dev_name);
            holder.buy = (TextView) row.findViewById(R.id.dev_buy);
            holder.sel= (TextView) row.findViewById(R.id.dev_sel);
            holder.date = (TextView) row.findViewById(R.id.dev_date);
            holder.spead= (TextView) row.findViewById(R.id.dev_spead);
            row.setTag(holder);
        } else {

            holder = (AdapterHolderDevice) row.getTag();

        }




          holder.device = getItem(position);

           holder.nameDevice.setText(holder.device.getNamedevice());
          // holder.nameChange.setText(devices.get(position).getNametochange());
           String buy_str = String.valueOf(holder.device.getBuyPrice());
           String sel_str = String.valueOf(holder.device.getSelPrice());

          int l = buy_str.length();
          int j = l-2;
          int s= sel_str.length();
          int k = s-2;

           Spannable span = new SpannableString(buy_str);
           Spannable span1 = new SpannableString(sel_str);


            span.setSpan(new RelativeSizeSpan(2f), j,l, 0);
            span1.setSpan(new RelativeSizeSpan(2f), k,s, 0);


           holder.buy.setText(span);
           holder.sel.setText(span1);
       String sp = String.valueOf(holder.device.getSpread());


        holder.spead.setText("spread:"+sp.substring(0,4));
         Calendar c = Calendar.getInstance();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String strDate = sdf.format(c.getTime());
         holder.date.setText(strDate);
        if(devices.get(position).getStat() == 0 ){

            holder.buy.setTextColor(Color.parseColor("#b10a0a"));
            holder.sel.setTextColor(Color.parseColor("#b10a0a"));

        }
        else{

            holder.buy.setTextColor(Color.parseColor("#6ba134"));
            holder.sel.setTextColor(Color.parseColor("#6ba134"));


        }
        final int DEFAULT_THRESHOLD = 128;

        row.setOnTouchListener(new View.OnTouchListener() {

            int initialX = 0;
            final float slop = ViewConfiguration.get(mcontext).getScaledTouchSlop();

            public boolean onTouch(final View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    initialX = (int) event.getX();
                    view.setPadding(0, 0, 0, 0);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int currentX = (int) event.getX();
                    int offset = currentX - initialX;
                    if (Math.abs(offset) > slop) {
                        view.setPadding(offset, 0, 0, 0);

                        if (offset > DEFAULT_THRESHOLD) {

                            if(position<devices.size()){


                                currencySql.deletechangefavoris(devices.get(position).getNamedevice());
                                currencySql.addChange(devices.get(position).getNamedevice());
                                remove(devices.get(position));
                                notifyDataSetChanged();
                            }

                            notifyDataSetChanged();
                        } else if (offset < -DEFAULT_THRESHOLD) {
                            if(position<devices.size()){


                                currencySql.deletechangefavoris(devices.get(position).getNamedevice());
                                currencySql.addChange(devices.get(position).getNamedevice());
                                remove(devices.get(position));
                                notifyDataSetChanged();
                            }

                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    // Animate back if no action was performed.
                    ValueAnimator animator = ValueAnimator.ofInt(view.getPaddingLeft(), 0);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            view.setPadding((Integer) valueAnimator.getAnimatedValue(), 0, 0, 0);
                        }
                    });
                    animator.setDuration(150);
                    animator.start();
                }
                return true;
            }
        });

        return row;


    }
}
