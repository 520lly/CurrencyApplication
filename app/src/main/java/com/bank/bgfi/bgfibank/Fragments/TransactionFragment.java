package com.bank.bgfi.bgfibank.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bank.bgfi.bgfibank.Adapter.DeviceAdapter;
import com.bank.bgfi.bgfibank.Model.Devise;
import com.bank.bgfi.bgfibank.R;
import com.bank.bgfi.bgfibank.Util.CurrencySql;
import com.bank.bgfi.bgfibank.Util.ServiceHandler;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Timer timer ;
    int nb = 0 ;
    Devise devise ;
    TextView buy_text;
    TextView sell_text;
    String url = "https://currencytrading.herokuapp.com/byone";
    private OnFragmentInteractionListener mListener;

    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
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
        View v = inflater.inflate(R.layout.fragment_transaction, container, false);

        devise = new Devise();
        devise=getArguments().getParcelable("device");

        buy_text = (TextView)v.findViewById(R.id.txt_buy_tran);
        sell_text =(TextView)v.findViewById(R.id.txt_sel_tran);


        timer = new Timer();


        timer.schedule(new TimerTask() {



            @Override
            public void run() {

                if (getActivity() !=null)
                {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            new GetPairTask().execute();
                        }
                    });
                }



            }
        }, 0, 5000);
        final TextView nb_trans = (TextView)v.findViewById(R.id.nb_trans);
nb_trans.setText(""+nb);

        ImageButton btn_add = (ImageButton)v.findViewById(R.id.add_btn_tran);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(nb);
                nb++;
                nb_trans.setText(""+nb);
            }
        });

        ImageButton btn_remove = (ImageButton)v.findViewById(R.id.romve_btn_tran);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nb>0){

                    nb--;

                }

                nb_trans.setText(""+nb);
            }
        });
        Button btn_buy =  (Button)v.findViewById(R.id.buy_button);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button btn_sell =  (Button)v.findViewById(R.id.sell_button);

btn_sell.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
});




        Spinner spinner = (Spinner)v.findViewById(R.id.spinner_transection);
        List<String> spinner_list = new ArrayList<>();
        spinner_list.add("choice1");
        spinner_list.add("choice2");
        spinner_list.add("choice3");
        spinner_list.add("choice4");
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_layout_row,spinner_list);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();


                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    // handle back button

                    android.support.v4.app.FragmentManager mg = getFragmentManager();
                    mg.beginTransaction().replace(R.id.frame, new ViewPagerFragment(), "frag").commit();
                    return true;

                }

                return false;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
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

    public class  GetPairTask extends AsyncTask <String,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected String doInBackground(String... params) {
            List<String> variable = new ArrayList<>();
            CurrencySql currencySql = new CurrencySql(getActivity());
            variable = currencySql.getAllChangefavoris();
            Gson g = new Gson();
            final  String f =  devise.getNamedevice();
            System.out.println("arrr   "+f);

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
            String json =  serviceHandler.makeServiceCall(url,1,valuePairList);
            System.out.println("ok   "+json);
            try {
                JSONObject j = new JSONObject(json);



                    devise.setBuyPrice(j.getDouble("buy"));
                    devise.setSelPrice(j.getDouble("sell"));
                    devise.setSpread(j.getDouble("spread"));
                    devise.setStat(j.getInt("stat"));
                } catch (JSONException e1) {
                e1.printStackTrace();
            }


            return  json ;




        }




        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(getActivity()!=null){


                String buy_str = String.valueOf(devise.getBuyPrice());
                String sel_str = String.valueOf(devise.getSelPrice());

                int l = buy_str.length();
                int j = l-2;
                int ss= sel_str.length();
                int k = ss-2;

                Spannable span = new SpannableString(buy_str);
                Spannable span1 = new SpannableString(sel_str);

                try{
                    span.setSpan(new RelativeSizeSpan(1.5f), j,l, 0);
                    span1.setSpan(new RelativeSizeSpan(1.5f), k,ss, 0);



                }

                catch(Exception e){}
                buy_text.setText(span);
                sell_text.setText(span1);
                if(devise.getStat() == 0 ){

                    buy_text.setTextColor(Color.parseColor("#b10a0a"));
                    sell_text.setTextColor(Color.parseColor("#b10a0a"));

                }
                else{

                    buy_text.setTextColor(Color.parseColor("#6ba134"));
                    sell_text.setTextColor(Color.parseColor("#6ba134"));


                }

            }


        }
    }



}
