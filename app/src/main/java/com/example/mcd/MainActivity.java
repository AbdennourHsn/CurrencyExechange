package com.example.mcd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.ContentValues.TAG;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Enumeration;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView tvR,ol,text1,text2 , updatetxt;
    Spinner sp1,sp2;
    EditText et1;

    ArrayAdapter<String> adapter ;
    ArrayList<String> currName = new ArrayList<String>();
    Double quote;
    Double firstCurrencyPrice=1d;
    Double secondCurrencyPrice=1d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvR=findViewById(R.id.tvR);
        sp1=findViewById(R.id.sp1);
        sp2=findViewById(R.id.sp2);
        et1=findViewById(R.id.e1);
        text1=findViewById(R.id.text1);
        text2=findViewById(R.id.text2);
        updatetxt=findViewById(R.id.date);
        Convert();
        RetrInt retrInt = RetBuild.getRetrofitInstance().create(RetrInt.class);
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Convert();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        sp1.setOnItemSelectedListener(this);
        sp2.setOnItemSelectedListener(this);
        Call<JsonObject> call = retrInt.getDateLastUpdate();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                Currency_ currency = gson.fromJson(response.body(),Currency_.class);
                updatetxt.setText(""+currency.updateTime);
                //Log.d(TAG,"Abd "+response.body());
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {Log.d(TAG , "Abd : Error" + t.toString());}});

    }

public  void Convert(){
    try {
        quote=secondCurrencyPrice/firstCurrencyPrice;
        tvR.setText(""+ new DecimalFormat("##.##").format(quote * Double.parseDouble(et1.getText().toString()) ));
        text1.setText(String.valueOf(""+1.00));
        text2.setText(""+new DecimalFormat("##.##").format(quote));
    }
    catch (Exception e){
        tvR.setText("0.00");
    }
}

//public void changeQuote(){
//    quote = GetPrice(sp2.getSelectedItem().toString())/GetPrice(sp1.getSelectedItem().toString());
//
//}


    public void assignNamesToSpinner(){
        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, currName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter);
        sp2.setAdapter(adapter);
    }

    private ArrayList<String> GetCurrenciesNemes(Hashtable<String, Double> map) {
        Enumeration<String> e = map.keys();
        ArrayList<String> cur = new ArrayList<String>();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            cur.add(key);
        }
        return cur;
    }

//    public double GetPrice(String name){
//        double value = 0;
//        Cursor q= db.readRow(name);
//        q.moveToNext();
//        value = Double.parseDouble(q.getString(2));
//        return  value;
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //changeQuote();
        RetrInt retrInt = RetBuild.getRetrofitInstance().create(RetrInt.class);
        Call<String> call2 = retrInt.getExchangeCurrency(sp2.getSelectedItem().toString());
        Call<String> call1 = retrInt.getExchangeCurrency(sp1.getSelectedItem().toString());
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                secondCurrencyPrice=Double.parseDouble(response.body());

                Log.d(TAG , "Abd "+sp2.getSelectedItem().toString()+" "+ secondCurrencyPrice);
                Convert();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {Log.d(TAG , "Abd : Error" + t.toString());}});

        call1.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                firstCurrencyPrice=Double.parseDouble(response.body());

                Log.d(TAG , "Abd "+ sp1.getSelectedItem().toString()+" :"+ firstCurrencyPrice);
                Convert();

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {Log.d(TAG , "Abd : Error" + t.toString());}});
        Convert();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class  Currency_{
        public String name;
        public Double price;
        public String updateTime;
    }

}