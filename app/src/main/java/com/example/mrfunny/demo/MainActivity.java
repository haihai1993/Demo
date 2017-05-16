package com.example.mrfunny.demo;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private API api;
    private Call<ResponseBody> call;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private Gson gson;
    private ArrayList<Quote> quotes;
    private Service service;
    private String dateFrom, dateTo;
    private EditText edtFrom, edtTo, currentEditext;
    private Calendar calendarCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtFrom = (EditText) findViewById(R.id.edtFrom);
        edtTo = (EditText) findViewById(R.id.edtTo);

        edtFrom.setOnClickListener(this);
        edtTo.setOnClickListener(this);

        calendarCurrent = Calendar.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Adapter(this);
        recyclerView.setAdapter(adapter);
        service = new Service();
        gson = new Gson();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendarCurrent.set(Calendar.YEAR, year);
            calendarCurrent.set(Calendar.MONTH, monthOfYear);
            calendarCurrent.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(currentEditext);
            if (currentEditext.getId() == R.id.edtTo) {
                getData();
            }
        }
    };

    @Override
    public void onClick(View v) {
        currentEditext = (EditText) v;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog dialog;
        if (v.getId() == R.id.edtTo) {
            calendar.set(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DATE));
            dialog = new DatePickerDialog(MainActivity.this, date,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.YEAR, calendarCurrent.get(Calendar.YEAR) + 3);
            dialog.getDatePicker().setMinDate(calendarCurrent.getTimeInMillis());
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        } else {
            dialog = new DatePickerDialog(MainActivity.this, date,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        dialog.show();

    }

    private void updateLabel(EditText editText) {
        String myFormat = "dd-MMM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        editText.setText(sdf.format(calendarCurrent.getTime()));
        myFormat = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(myFormat);
        if (editText.getId() == R.id.edtFrom) {
            dateFrom = sdf.format(calendarCurrent.getTime());
        } else {
            dateTo = sdf.format(calendarCurrent.getTime());
        }
    }

    private void getData() {
        api = service.getRetrofit().create(API.class);
        final String q = "select*from yahoo.finance.historicaldata where symbol = \"AAPL\""
                + " and startDate = \"" + dateFrom + "\" and endDate = \"" + dateTo + "\"";

        call = api.getData(q, "json", true, "store://datatables.org/alltableswithkeys", "");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String query, result, quote = "";
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    query = jsonObject.getString("query");
                    jsonObject = new JSONObject(query);
                    result = jsonObject.getString("results");
                    jsonObject = new JSONObject(result);
                    quote = jsonObject.getString("quote");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                quotes = gson.fromJson(quote, new TypeToken<List<Quote>>() {
                }.getType());

                if (quotes != null) {
                    adapter.initData(quotes);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
