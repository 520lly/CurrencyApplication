package com.bank.bgfi.bgfibank.Adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ayach on 8/12/16.
 */
public class DeviceFavorisAdapter extends ArrayAdapter {
    private   List <String> devises = new ArrayList<>();
    private Context mcontext ;
    private int resourceId ;
    CurrencySql currencySql;

    public DeviceFavorisAdapter(Context context, int resource, List<String> devises) {
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

                            if(position<devises.size()){


                                currencySql.deletechangefavoris(devises.get(position));
                                currencySql.addChange(devises.get(position));
                                remove(devises.get(position));
                                notifyDataSetChanged();
                            }

                            notifyDataSetChanged();
                        } else if (offset < -DEFAULT_THRESHOLD) {
                            if(position<devises.size()){


                                currencySql.deletechangefavoris(devises.get(position));
                                currencySql.addChange(devises.get(position));
                                remove(devises.get(position));
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
    private String getStringResourceByName(String aString) {
        String packageName = mcontext.getPackageName();
        int resId = mcontext.getResources().getIdentifier(aString, "string", packageName);
        return mcontext.getString(resId);
    }
}
