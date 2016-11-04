package com.bank.bgfi.bgfibank.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bank.bgfi.bgfibank.Adapter.DeviceFavorisAdapter;
import com.bank.bgfi.bgfibank.Adapter.DeviseForAllAdapter;
import com.bank.bgfi.bgfibank.R;
import com.bank.bgfi.bgfibank.Util.CurrencySql;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Add_devise_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Add_devise_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_devise_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView listView;
    CurrencySql currencySql;
    List<String> currency ;
    DeviseForAllAdapter deviceFavorisAdapter;

    private OnFragmentInteractionListener mListener;

    public Add_devise_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Add_devise_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Add_devise_Fragment newInstance(String param1, String param2) {
        Add_devise_Fragment fragment = new Add_devise_Fragment();
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
        View v = inflater.inflate(R.layout.fragment_add_devise_fragment, container, false);
        listView = (ListView) v.findViewById(R.id.list_add);
        currencySql = new CurrencySql(getActivity());
        currency = new ArrayList();
        currency = currencySql.getAllChange();
        deviceFavorisAdapter = new DeviseForAllAdapter(getActivity(),R.layout.fav_row_layout,currency);
        listView.setAdapter(deviceFavorisAdapter);
        setHasOptionsMenu(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currencySql.deletechange(currency.get(position));
                currencySql.addChangefavoris(currency.get(position));
                deviceFavorisAdapter.remove(currency.get(position));
                deviceFavorisAdapter.notifyDataSetChanged();
            }
        });
    return  v;
    }



    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
       // inflater.inflate(R.menu.add_setting_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
