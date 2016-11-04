package com.bank.bgfi.bgfibank.View;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;

import com.bank.bgfi.bgfibank.Adapter.Tabs_Viewpager_Adapter;
import com.bank.bgfi.bgfibank.Fragments.Add_devise_Fragment;
import com.bank.bgfi.bgfibank.Fragments.ChartFragment;
import com.bank.bgfi.bgfibank.Fragments.HistoryFragment;
import com.bank.bgfi.bgfibank.Fragments.List_change_fragment;
import com.bank.bgfi.bgfibank.Fragments.SettingFragment;
import com.bank.bgfi.bgfibank.Fragments.TradeFragment;
import com.bank.bgfi.bgfibank.Fragments.TransactionFragment;
import com.bank.bgfi.bgfibank.Fragments.ViewPagerFragment;
import com.bank.bgfi.bgfibank.R;

public class MainActivity extends AppCompatActivity implements List_change_fragment.OnFragmentInteractionListener
,ChartFragment.OnFragmentInteractionListener,HistoryFragment.OnFragmentInteractionListener,TradeFragment.OnFragmentInteractionListener
,TransactionFragment.OnFragmentInteractionListener,ViewPagerFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener,Add_devise_Fragment.OnFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ActionBar action = getSupportActionBar();
        action.hide();


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame,new ViewPagerFragment());
        ft.commit();




    }





    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }


}
