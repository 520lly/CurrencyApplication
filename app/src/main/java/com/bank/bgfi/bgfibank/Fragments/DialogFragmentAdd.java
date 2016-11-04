package com.bank.bgfi.bgfibank.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import com.bank.bgfi.bgfibank.R;
import com.bank.bgfi.bgfibank.Util.CurrencySql;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayach on 8/15/16.
 */
public class DialogFragmentAdd extends DialogFragment {

    CurrencySql currencySql ;
    List<String> mSelectedItems ;
    List<String> choiceuser;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mSelectedItems = new ArrayList();
        choiceuser = new ArrayList<>();
        currencySql = new CurrencySql(getActivity());
      mSelectedItems =  currencySql.getAllChange();

        final CharSequence[] items = mSelectedItems.toArray(new CharSequence[mSelectedItems.size()]);// Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        // Set the dialog title
        builder.setTitle("Add currency")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(items, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    choiceuser.add(mSelectedItems.get(which));

                                } else if (choiceuser.contains(mSelectedItems.get(which))) {

                                 choiceuser.remove(mSelectedItems.get(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        for (String s:choiceuser) {
                            currencySql.addChangefavoris(s);
                            currencySql.deletechange(s);
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.frame,new ViewPagerFragment());
                            ft.commit();
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

       AlertDialog a = builder.create();

        a.show();
        Button bq = a.getButton(DialogInterface.BUTTON_NEGATIVE);
        bq.setTextColor(Color.BLUE);
        Button bqd = a.getButton(DialogInterface.BUTTON_POSITIVE);
        bqd.setTextColor(Color.BLUE);

        return a;


    }




}
