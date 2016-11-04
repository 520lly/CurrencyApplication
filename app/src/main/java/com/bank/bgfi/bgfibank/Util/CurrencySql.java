package com.bank.bgfi.bgfibank.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by ayach on 8/11/16.
 */
public class CurrencySql extends SQLiteOpenHelper {

    private static final  String Tablename ="table_change";
    private static final String ChangeContenu="name_change_col";
    private static final String CREATE_BDD = "CREATE TABLE " + Tablename + " ("
            + ChangeContenu  + " TEXT PRIMARY KEY );";

    private static final  String Tablenamefavoris ="table_change_favoris";
    private static final String ChangeContenufavoris="name_change_col_favoris";
    private static final String CREATE_BDD_favoris = "CREATE TABLE " + Tablenamefavoris + " ("
            + ChangeContenufavoris  + " TEXT PRIMARY KEY );";


    private static final  String Tablenamecharts ="table_charts";
    private static final String charts_data="charts_data";
    private static final String CREATE_BDD_charts = "CREATE TABLE " + Tablenamecharts + " ("
            + charts_data  + " TEXT PRIMARY KEY );";

    private static final  String Tablenamecurrency ="table_currency";
    private static final String currency_data="currency_data";
    private static final String CREATE_BDD_currency = "CREATE TABLE " + Tablenamecurrency + " ("
            + currency_data  + " TEXT PRIMARY KEY );";



    private static final  String INSERTTABLE = "INSERT INTO "+Tablename+" ("+ChangeContenu+") VALUES" +
            "('CHF/JPY')," +
            "('CAD/JPY')," +
            "('USD/MXN')," +
            "('EUR/NOK')," +
            "('USD/SEK')," +
            "('EUR/TRY')," +
            "('XAG/USD')," +
            "('AUD/NZD');";



    private static final  String INSERTTABLEFAVORIS = "INSERT INTO "+Tablenamefavoris+" ("+ChangeContenufavoris+") VALUES" +
            "('EUR/USD')," +
            "('EUR/GBP')," +
            "('USD/CAD')," +
            "('EUR/JPY')," +
            "('USD/CHF')," +
            "('EUR/CHF')," +
            "('USD/SGD')," +
            "('AUD/USD');";


    private static final  String INSERTTABLECurrency = "INSERT INTO "+Tablenamecurrency+" ("+currency_data+") VALUES" +
            "('[{\"change\":\"EUR/USD\",\"buy\":\"1.12647\",\"sell\":\"1.12677\",\"spread\":0.00019999999999998,\"stat\":1},{\"change\":\"EUR/GBP\",\"buy\":\"0.86463\",\"sell\":\"0.86456\",\"spread\":0.00012999999999996,\"stat\":0},{\"change\":\"USD/CAD\",\"buy\":\"1.2901\",\"sell\":\"1.28951\",\"spread\":0.00025000000000008,\"stat\":1},{\"change\":\"GBP/USD\",\"buy\":\"1.30273\",\"sell\":\"1.30319\",\"spread\":0.00024000000000002,\"stat\":1}]');";



    public CurrencySql(Context context) {
        super(context, Tablename, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
        db.execSQL(CREATE_BDD_favoris);
        db.execSQL(INSERTTABLE);
        db.execSQL(INSERTTABLEFAVORIS);
        db.execSQL(CREATE_BDD_charts);
        db.execSQL(CREATE_BDD_currency);
        db.execSQL(INSERTTABLECurrency);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Tablename + ";");
        db.execSQL("DROP TABLE " + Tablenamefavoris + ";");
        db.execSQL("DROP TABLE " + Tablenamecharts + ";");
        db.execSQL("DROP TABLE " + Tablenamecurrency + ";");
        onCreate(db);
    }


    public ArrayList<String> getAllChange() {
        ArrayList<String> ListChange = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Tablename;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                String l = cursor.getString(0);
                ListChange.add(l);
            } while (cursor.moveToNext());
        }

        return ListChange;
    }

    public ArrayList<String> getAllChangefavoris() {
        ArrayList<String> ListChangefavoris = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Tablenamefavoris;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                String l = cursor.getString(0);
                ListChangefavoris.add(l);
            } while (cursor.moveToNext());
        }

        return ListChangefavoris;
    }



    public void addChange(String ch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ChangeContenu,ch);
        db.insert(Tablename, null, values);
        db.close();


    }



    public void addChangefavoris(String ch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ChangeContenufavoris,ch);
        db.insert(Tablenamefavoris, null, values);
        db.close();
    }

    public void deletechange(String ch) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Tablename, ChangeContenu + " = ?",
                new String[] { String.valueOf(ch) });
        db.close();

    }


    public void deletechangefavoris(String ch) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Tablenamefavoris, ChangeContenufavoris + " = ?",
                new String[] { String.valueOf(ch) });
        db.close();

    }

    public void addChart(String ch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(charts_data,ch);
        db.insert(Tablenamecharts, null, values);
        db.close();


    }

    public void addCurrency(String ch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(currency_data,ch);
        db.insert(Tablenamecurrency, null, values);
        db.close();


    }
    public void deleteCharts(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ Tablenamecharts);
        db.close();

    }


    public void deleteCurrency(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ Tablenamecurrency);
        db.close();

    }


    public String getAllCurrency() {
        String l =null;
        String selectQuery = "SELECT  * FROM " + Tablenamecurrency;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                l = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        return l;
    }


    public String getAllcharts() {
        String l =null;
        String selectQuery = "SELECT  * FROM " + Tablenamecharts;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                l = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        return l;
    }
}
