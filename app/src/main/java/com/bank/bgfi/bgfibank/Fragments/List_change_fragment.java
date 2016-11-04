package com.bank.bgfi.bgfibank.Fragments;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bank.bgfi.bgfibank.Adapter.DeviceAdapter;
import com.bank.bgfi.bgfibank.Adapter.RecycleViewAdapter;
import com.bank.bgfi.bgfibank.Model.Devise;
import com.bank.bgfi.bgfibank.R;
import com.bank.bgfi.bgfibank.Util.CurrencySql;
import com.bank.bgfi.bgfibank.Util.ServiceHandler;
import com.google.gson.Gson;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.io.SessionOutputBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link List_change_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link List_change_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class List_change_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FloatingActionButton floatingActionButton ;
    Timer timer ;
    String url ="https://currencytrading.herokuapp.com/async";
    RecyclerView changeList ;
    //http://192.168.56.1:3000/async
    RecycleViewAdapter deviceAdapter;
    ItemTouchHelper itemTouchHelper;
    ItemTouchHelper.SimpleCallback  simpleItemTouchCallback;
    CurrencySql currencySql;
    private Paint p = new Paint();
    private OnFragmentInteractionListener mListener;

    public List_change_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment List_change_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static List_change_fragment newInstance(String param1, String param2) {
        List_change_fragment fragment = new List_change_fragment();
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
        View v = inflater.inflate(R.layout.fragment_list_change_fragment, container, false);
        changeList = (RecyclerView) v.findViewById(R.id.list_devise);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        changeList.setLayoutManager(llm);
        currencySql = new CurrencySql(getActivity());
        final FloatingActionButton floatingActionButton = (FloatingActionButton)v.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DialogFragmentAdd dialogFragmentAdd = new DialogFragmentAdd();
                dialogFragmentAdd.show(fm,"stg");

            }
        });

        timer = new Timer();


            timer.schedule(new TimerTask() {



                @Override
                public void run() {

                    if (getActivity() !=null)
                    {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                new ListTask().execute();
                            }
                        });
                    }



                }
            }, 0, 8000);




   /*     changeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putParcelable("device",deviceAdapter.getItem(position));
                TransactionFragment transactionFragment = new TransactionFragment();
                transactionFragment.setArguments(bundle);
                ft.replace(R.id.frame,transactionFragment);
                ft.commit();



            }
        });*/





         simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView

                if (swipeDir == ItemTouchHelper.RIGHT){

                    String s= deviceAdapter.getItem(viewHolder.getAdapterPosition()).getNamedevice();
                    deviceAdapter.remove(viewHolder.getAdapterPosition());
                    currencySql.deletechangefavoris(s);
                    currencySql.addChange(s);
                   }


                }



            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

        @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {

            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;
            Bitmap icon;

                if (dX > 0) {

                    p.setColor(Color.parseColor("#ff6666"));
                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                    c.drawRect(background, p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white_36dp);
                    RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);

            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }





        };
         itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(changeList);

        changeList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && floatingActionButton.isShown())
                    floatingActionButton.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    floatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

setHasOptionsMenu(true);
        return  v ;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.setting_reource_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
               SettingFragment settingFragment = new SettingFragment();
                ft.replace(R.id.frame,settingFragment);
                ft.commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class  ListTask extends AsyncTask <String,Void,String>{
        ArrayList <Devise> devises = new ArrayList<Devise>() ;
        CurrencySql currencySql = new CurrencySql(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected String doInBackground(String... params) {
            List<String> variable = new ArrayList<>();

            variable = currencySql.getAllChangefavoris();
            Gson g = new Gson();
        final  String f =  g.toJson(variable);
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

            String json = null;
            if(haveNetworkConnection()){

                json =  serviceHandler.makeServiceCall(url,1,valuePairList);
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i=0;i<jsonArray.length();i++){
                        Devise d = new Devise();
                        JSONObject j = jsonArray.getJSONObject(i);

                        d.setBuyPrice(j.getDouble("buy"));
                        d.setSelPrice(j.getDouble("sell"));
                        d.setNamedevice(j.getString("change"));
                        d.setSpread(j.getDouble("spread"));
                        d.setStat(j.getInt("stat"));
                        devises.add(d);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else{

                json = currencySql.getAllCurrency();
                System.out.println(json);
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i=0;i<jsonArray.length();i++){
                        Devise d = new Devise();
                        JSONObject j = jsonArray.getJSONObject(i);
                        for (String s:variable) {
                            if(j.getString("change").equals(s)){

                                d.setBuyPrice(j.getDouble("buy"));
                                d.setSelPrice(j.getDouble("sell"));
                                d.setNamedevice(j.getString("change"));
                                d.setSpread(j.getDouble("spread"));
                                d.setStat(j.getInt("stat"));
                                devises.add(d);

                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            return  json ;




        }




        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(getActivity()!=null){

               // deviceAdapter = new DeviceAdapter(getActivity(),R.layout.layout_device_adapter,devises);

                deviceAdapter = new RecycleViewAdapter(devises,currencySql.getAllChangefavoris());
                changeList.setAdapter(deviceAdapter);
                deviceAdapter.notifyDataSetChanged();

            }


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
