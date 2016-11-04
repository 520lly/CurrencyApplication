package com.bank.bgfi.bgfibank.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bank.bgfi.bgfibank.R;
import com.bank.bgfi.bgfibank.Util.AxisForma;
import com.bank.bgfi.bgfibank.Util.CurrencySql;
import com.bank.bgfi.bgfibank.Util.ServiceHandler;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {
    Typeface mTfLight ;
    ArrayList<String> timelist =new ArrayList<>() ;
    private final int itemcount = 12;
    int count = 0;
    private CombinedChart mChart;
     Spinner spinnerchart ;
    CandleData d = new CandleData();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String pair = "EUR/USD";
    Long tsLong = System.currentTimeMillis()/1000 - 20000;
    String ts = tsLong.toString();

    String url ="http://webcharts.fxserver.com/charts/activeChartFeed.php?pair="+pair+"&period=0&unit=&limit=80&timeout="+ts+"&rateType=bid&GMT=on";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v =inflater.inflate(R.layout.fragment_chart, container, false);

        mTfLight  = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        mChart = (CombinedChart) v.findViewById(R.id.chartcombined);
        spinnerchart = (Spinner) v.findViewById(R.id.spinnerchange);

        spinnerchart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (!(position == 0)){
                    pair = parent.getItemAtPosition(position).toString();

                    String url2 = "http://webcharts.fxserver.com/charts/activeChartFeed.php?pair="+pair+"&period=0&unit=&limit=80&timeout=1469704460&rateType=bid&GMT=on";

                    System.out.println(url2);
                    new TransactionTask().execute(url2);


                }




                   //   System.out.println(url2);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

     new TransactionTask().execute(url);


return  v ;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public void chartpandding () {

        List<String> spinner_list = new ArrayList<>();
        CurrencySql currencySql = new CurrencySql(getActivity());
        spinner_list = currencySql.getAllChangefavoris();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,spinner_list);

        spinnerchart.setAdapter(arrayAdapter);
        mChart.setDescription("");
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE
        });

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinValue(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new AxisForma(timelist));
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(false);


        mChart.resetTracking();
        mChart.animateXY(3000,3000);



        CombinedData data = new CombinedData();

        data.setData(d);
        data.setValueTypeface(mTfLight);

        xAxis.setAxisMaxValue(data.getXMax()+0.005f);
        mChart.moveViewToX(data.getXMax());
        mChart.setScaleMinima(3f,0);
        mChart.setData(data);

    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    class TransactionTask extends AsyncTask<String,Void,String> {



        ProgressDialog barProgressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }




        @Override
        protected String doInBackground(String... params) {
            ServiceHandler serviceHandler = new ServiceHandler();
            ArrayList<CandleEntry> entries = new ArrayList<CandleEntry>();
            if(haveNetworkConnection()){

                String json =  serviceHandler.makeServiceCall(url,1);
                JSONArray datajson = null;
                JSONArray timejson = null;
                JSONObject spread =null;

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject candles =   jsonObject.getJSONObject("candles");
                    datajson = candles.getJSONArray("data");
                    timejson = candles.getJSONArray("time");
                    //  spread = jsonObject.getJSONObject("spread");



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                float  valueopen = 0 , valueclose= 0 ,valueH =0 ,valueL =0;
                int index =0 ;
                String time;

                try {

                    if(datajson.length()==0){}
                    else{

                        for ( index = 0; index < datajson.length(); index ++){
                            JSONObject candle = datajson.getJSONObject(index);

                            valueopen = Float.parseFloat(candle.getString("O")) ;
                            valueclose = Float.parseFloat(candle.getString("C")) ;
                            valueH = Float.parseFloat(candle.getString("H")) ;
                            valueL = Float.parseFloat(candle.getString("L")) ;

                            time =  timejson.getString(index) ;
                            timelist.add(time);

                            entries.add(new CandleEntry(index, valueH, valueL, valueopen, valueclose));
                        }

                    }} catch (JSONException e) {
                    e.printStackTrace();






                }





            }
            else{


                CurrencySql currencySql = new CurrencySql(getActivity());
                String json =  currencySql.getAllcharts();
                JSONArray datajson = null;
                JSONArray timejson = null;
                JSONObject spread =null;

                try {


                        JSONObject jsonObject = new JSONObject(json);

                        JSONObject candles =   jsonObject.getJSONObject("candles");
                        datajson = candles.getJSONArray("data");
                        timejson = candles.getJSONArray("time");
                        //  spread = jsonObject.getJSONObject("spread");
                        float  valueopen = 0 , valueclose= 0 ,valueH =0 ,valueL =0;
                        int index =0 ;
                        String time;

                        try {


                            for ( index = 0; index < datajson.length(); index ++){
                                JSONObject candle = datajson.getJSONObject(index);

                                valueopen = Float.parseFloat(candle.getString("O")) ;
                                valueclose = Float.parseFloat(candle.getString("C")) ;
                                valueH = Float.parseFloat(candle.getString("H")) ;
                                valueL = Float.parseFloat(candle.getString("L")) ;

                                time =  timejson.getString(index) ;
                                timelist.add(time);

                                entries.add(new CandleEntry(index, valueH, valueL, valueopen, valueclose));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();






                        }








                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }






            CandleDataSet set = new CandleDataSet(entries, "Candle DataSet");
            set.setDecreasingColor(Color.rgb(142, 150, 175));
            set.setShadowColor(Color.DKGRAY);
            set.setBarSpace(0.3f);
            set.setValueTextSize(10f);
            set.setDrawValues(false);
            d.addDataSet(set);



            return null;




        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


           chartpandding();






        }



    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
