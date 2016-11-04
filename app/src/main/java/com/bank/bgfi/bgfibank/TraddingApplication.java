package com.bank.bgfi.bgfibank;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.bank.bgfi.bgfibank.Util.CurrencySql;
import com.bank.bgfi.bgfibank.Util.ServiceHandler;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayach on 8/17/16.
 */
public class TraddingApplication extends Application {

    String url = "https://currencytrading.herokuapp.com/async";
    String url2= "http://webcharts.fxserver.com/charts/activeChartFeed.php?pair=EUR/USD&period=0&unit=&limit=80&timeout=1469984460&rateType=bid&GMT=on" ;


    public void onCreate() {
        super.onCreate();

        if(haveNetworkConnection()){

new ApplicationTask().execute();


        }







    }

    class ApplicationTask extends AsyncTask<String,Void,String> {





        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }




        @Override
        protected String doInBackground(String... params) {
            CurrencySql currencySql = new CurrencySql(getApplicationContext());
            List<String> variable = new ArrayList<>();
            variable.add("EUR/USD");variable.add("EUR/GBP");variable.add("USD/CAD");variable.add("GBP/USD");
            variable.add("EUR/JPY");variable.add("USD/CHF");variable.add("EUR/CHF");variable.add("AUD/USD");
            variable.add("CHF/JPY");variable.add("CAD/JPY");variable.add("USD/MXN");variable.add("EUR/NOK");
            variable.add("USD/SEK");variable.add("EUR/TRY");variable.add("XAG/USD");variable.add("AUD/NZD");
            Gson g = new Gson();
            final  String f =  g.toJson(variable);
            ServiceHandler serviceHandler = new ServiceHandler();
            NameValuePair nameValuePair = new NameValuePair() {
                @Override
                public String getName() {
                    return "dev";
                }

                @Override
                public String getValue() {
                    return f;
                }
            };
            List<NameValuePair> valuePairList = new ArrayList<>() ;
            valuePairList.add(nameValuePair);
            String currency =  serviceHandler.makeServiceCall(url,1,valuePairList);
            String charts = serviceHandler.makeServiceCall(url2,1);
            currencySql.deleteCharts();
            currencySql.deleteCurrency();
            currencySql.addCurrency(currency);
            currencySql.addChart(charts);
            return null;

        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }



    }





    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
