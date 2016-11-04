package com.bank.bgfi.bgfibank.Adapter;

import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bank.bgfi.bgfibank.Model.Devise;
import com.bank.bgfi.bgfibank.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by ayach on 8/18/16.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.AdapterHolderDevice> {



    private ArrayList<Devise> devises = new ArrayList<Devise>();
    private ArrayList<String> change = new ArrayList<>();
    HashMap<String,Devise> hashMap = new HashMap<>();


    public RecycleViewAdapter(ArrayList<Devise>list ,ArrayList<String> change){
        this.devises=list;
        this.change = change;
        for (int i =0 ; i<devises.size();i++){

            hashMap.put(change.get(i),devises.get(i));

        }

    }

    @Override
    public AdapterHolderDevice onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.layout_device_adapter, parent, false);
        return  new AdapterHolderDevice(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterHolderDevice holder, int position) {

        Devise d = hashMap.get(change.get(position));


        holder.nameDevice.setText(d.getNamedevice());
        // holder.nameChange.setText(devices.get(position).getNametochange());
        String buy_str = String.valueOf(d.getBuyPrice());
        String sel_str = String.valueOf(d.getSelPrice());

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
        String sp = String.valueOf(d.getSpread());


        holder.spead.setText("spread:"+sp.substring(0,3));
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        holder.date.setText(strDate);
        if(d.getStat() == 0 ){

            holder.buy.setTextColor(Color.parseColor("#b10a0a"));
            holder.sel.setTextColor(Color.parseColor("#b10a0a"));

        }
        else{

            holder.buy.setTextColor(Color.parseColor("#6ba134"));
            holder.sel.setTextColor(Color.parseColor("#6ba134"));


        }



    }



    public void remove(int position) {
        devises.remove(position);
        notifyDataSetChanged();



    }


    public int getindex(String s) {


       return devises.indexOf(s);

    }

    public Devise getItem(int position) {

        return devises.get(position);
    }

    @Override
    public int getItemCount() {
        return devises.size();
    }

    public class AdapterHolderDevice extends AnimateViewHolder {
        Devise device;
        TextView nameDevice;
        TextView buy;
        TextView sel;
        TextView date;
        TextView spead;

        public AdapterHolderDevice(View itemView) {
            super(itemView);
            nameDevice = (TextView) itemView.findViewById(R.id.dev_name);
            buy = (TextView) itemView.findViewById(R.id.dev_buy);
            sel= (TextView) itemView.findViewById(R.id.dev_sel);
            date = (TextView) itemView.findViewById(R.id.dev_date);
            spead= (TextView) itemView.findViewById(R.id.dev_spead);
        }

        @Override
        public void animateAddImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.setTranslationY(itemView, +itemView.getHeight() * 0.3f);
            ViewCompat.setAlpha(itemView, 0.5f);
        }

        @Override
        public void preAnimateAddImpl() {
            ViewCompat.setTranslationY(itemView, -itemView.getHeight() * 0.3f);
            ViewCompat.setAlpha(itemView, 0f);
        }

        @Override
        public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {

            ViewCompat.animate(this.itemView)
                    .translationY(0)
                    .alpha(1f)
                    .setDuration(300)
                    .setListener(listener)
                    .start();

        }


    }





}
