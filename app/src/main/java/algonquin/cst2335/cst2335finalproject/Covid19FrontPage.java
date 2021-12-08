package algonquin.cst2335.cst2335finalproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Covid19FrontPage extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    String searchItem ;
    RecyclerView covidRecycleView;
    private String stringURL;
    ArrayList<CovidInfo> myCovidInfo = new ArrayList<>();
    MyCovidAdapter adt;
    AlertDialog loadDialogue;
    Button snkBr;
    SQLiteDatabase db;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19_front_page);


        sharedpreferences = getSharedPreferences("CovidPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        EditText searchBar = (EditText) findViewById(R.id.searchBar);
        covidRecycleView = findViewById(R.id.covidRecycleView);
        adt = new MyCovidAdapter();
        covidRecycleView.setAdapter(adt);
        covidRecycleView.setLayoutManager(new LinearLayoutManager(this));

        MyCovidHelper opener = new MyCovidHelper( this );
        db =opener.getWritableDatabase();


        snkBr = findViewById(R.id.snkBar);
        snkBr.setOnClickListener(clk -> {
            searchItem = searchBar.getText().toString();
            Context context = getApplicationContext();
            Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show();
            editor.putString("searchedItem", searchItem);
            editor.apply();

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Fetching the data after: ")
                    .setMessage(searchItem)
                    .setView( new ProgressBar(this))
                    .show();

            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute( () -> {




                try {
                    String cityName = searchBar.getText().toString();
                    stringURL = "https://api.covid19tracker.ca/reports?after="
                            + URLEncoder.encode(cityName, "UTF-8");
                    URL url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));


                    JSONObject theDocument = new JSONObject(text);
                    //   JSONArray theArray = new JSONArray(text);
                    JSONArray covidArray = theDocument.getJSONArray("data");
                    myCovidInfo.clear();

                    for (int i = 0; i < covidArray.length(); i++) {

                        String name = theDocument.getString("province");

                        JSONObject objAtI = covidArray.getJSONObject(i);

                        int totalVaccinations = objAtI.getInt("total_vaccinations");
                        int totalRecoveries = objAtI.getInt("total_recoveries");
                        int totalFatalities = objAtI.getInt("total_fatalities");
                        int totalCases = objAtI.getInt("total_cases");
                        int totalHospital = objAtI.getInt("total_hospitalizations");
                        String daydate = objAtI.getString("date");
                        CovidInfo thisInfo = new CovidInfo(daydate,totalCases,totalFatalities,totalVaccinations,totalRecoveries,totalHospital);
                        myCovidInfo.add(thisInfo);
                        runOnUiThread(() ->{

                           // adt.notifyItemInserted(myCovidInfo.size()-1);
                            adt.notifyDataSetChanged();
                            dialog.hide();
                        });


                    }

                }
                catch (IOException | JSONException ioe){
                    Log.e("Connection Error", ioe.getMessage());
                }
            } );

        });


        searchItem = sharedpreferences.getString("searchedItem", "2222");
        searchBar.setText(searchItem);
        Button searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(v -> {
            searchItem = searchBar.getText().toString();
            Context context = getApplicationContext();
            Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show();
            editor.putString("searchedItem", searchItem);
            editor.apply();


        });

 /*       Button snackBar = findViewById(R.id.snkBar);
        snackBar.setOnClickListener(clsn -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Covid19FrontPage.this);
            builder.setMessage("Do you want to implement more functions?")
                    .setTitle("Question:")
                    .setNegativeButton("No", ((dialog, cl) -> {
                        searchBar.setText("GM");
                    }))
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        Snackbar.make(searchIcon, "Message", Snackbar.LENGTH_SHORT)
                                .setAction("hey", click -> {

                                 //searchBar.setText("Yes we will!");
                                })
                                .show();
                    })
                    .create().show();
        });*/
    }



    private class CovidRowViews extends RecyclerView.ViewHolder{

        TextView dateData;
        TextView casesData;
        TextView fatalitiesData;
        TextView hospitalData;
        TextView vaccinationData;
        TextView recoveryData;
        int position = 0;
        public CovidRowViews( View itemView) {
            super(itemView);
            dateData = itemView.findViewById(R.id.dateData);
            vaccinationData = itemView.findViewById(R.id.vaccinationData);
            fatalitiesData = itemView.findViewById(R.id.fatalitiesData);
             recoveryData = itemView.findViewById(R.id.recoveryData);
            hospitalData = itemView.findViewById(R.id.hospitalizationData);
            casesData = itemView.findViewById(R.id.caseData);

            itemView.setOnClickListener(click->{
                AlertDialog.Builder builder = new AlertDialog.Builder( Covid19FrontPage.this);
                builder.setMessage("Do you want to save data for the date: " + dateData.getText())
                        .setTitle("Question: ")
                        .setNegativeButton("No", ((dialog, cl) -> {
                        }))
                        .setPositiveButton("Yes",((dialog, cl) -> {
                  //  CovidInfo = new CovidInfo(dateData.getText(),casesData.getText(),fatalitiesData.getText(),hospitalData.getText(),vaccinationData.getText(),recoveryData.getText());
                            ContentValues savingRow = new ContentValues();
                            savingRow.put(MyCovidHelper.col_date, (String) dateData.getText());
                            savingRow.put(MyCovidHelper.col_cases, (String) casesData.getText());
                            savingRow.put(MyCovidHelper.col_fatalities, (String) fatalitiesData.getText());
                            savingRow.put(MyCovidHelper.col_hospitalizations, (String) hospitalData.getText());
                            savingRow.put(MyCovidHelper.col_vaccinations, (String) vaccinationData.getText());
                            savingRow.put(MyCovidHelper.col_recoveries, (String) recoveryData.getText());
                            db.insert(MyCovidHelper.TABLE_NAME,MyCovidHelper.col_cases,savingRow);


                            Snackbar.make(dateData,"Data saved", Snackbar.LENGTH_LONG)
                                    .setAction("Undo saving", clk->{
                                        CovidInfo removedDate =myCovidInfo.get(position);
                                        db.delete(MyCovidHelper.TABLE_NAME,"Date=?",new String[]{(String) dateData.getText()} );


                                    }).show();

                        })).create().show();


            });
        }
        public void setPosition(int p) {
            position = p;
        }
    }




    public class MyCovidAdapter extends RecyclerView.Adapter<CovidRowViews>{




        @Override
        public CovidRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View loadedRow = inflater.inflate(R.layout.covid_searched_item, parent, false);

            return new CovidRowViews(loadedRow);
        }
        @Override
        public void onBindViewHolder(CovidRowViews holder, int position) {

            runOnUiThread(() ->{
            /*holder.dateData.setText("Date: " + myCovidInfo.get(position).getSearchedDate());
            holder.casesData.setText("Total Cases: " + String.valueOf(myCovidInfo.get(position).getTotalCases()));
            holder.fatalitiesData.setText("Total Fatalities: " + String.valueOf(myCovidInfo.get(position).getTotalFatalities()));
            holder.hospitalData.setText("Total Hospitalizations: " +  String.valueOf(myCovidInfo.get(position).getTotalHospitalization()));
            holder.vaccinationData.setText("Total Vaccinations: " +String.valueOf(myCovidInfo.get(position).getTotalVaccinations()));
            holder.recoveryData.setText("Total Recoveries: " +String.valueOf(myCovidInfo.get(position).getTotalRecoveries()));
            holder.setPosition(position);*/
                holder.dateData.setText("Date:" + myCovidInfo.get(position).getSearchedDate());
                holder.casesData.setText("Total Cases:" + String.valueOf(myCovidInfo.get(position).getTotalCases()));
                holder.vaccinationData.setText("Total Vaccinations:" +String.valueOf(myCovidInfo.get(position).getTotalVaccinations()));
                holder.fatalitiesData.setText("Total Fatalities:" +String.valueOf(myCovidInfo.get(position).getTotalFatalities()));
                holder.recoveryData.setText("Total Recoveries:" +String.valueOf(myCovidInfo.get(position).getTotalRecoveries()));
                holder.hospitalData.setText("Total Hospitalizations:" +String.valueOf(myCovidInfo.get(position).getTotalHospitalization()));
                holder.setPosition(position);

            });

        }

        @Override
        public int getItemCount() {
            return myCovidInfo.size();
        }
        public int getItemViewType(int position) {
            CovidInfo thisRow = myCovidInfo.get(position);
            return super.getItemViewType(position);
        }
    }


    private class CovidInfo{


        String searchedDate;
        int totalCases;
        int totalFatalities;

        int totalVaccinations;
        int totalRecoveries;
        int totalHospitalization;
        long id;
        public void setId(long l){ id = l;}
        public long getId(){return id;}

        public CovidInfo(String searchedDate ,int totalCases, int totalFatalities, int totalVaccinations, int totalRecoveries,int totalHospitalization) {

            this.searchedDate = searchedDate;
            this.totalCases = totalCases;
            this.totalFatalities = totalFatalities;

            this.totalVaccinations = totalVaccinations;
            this.totalRecoveries = totalRecoveries;
            this.totalHospitalization = totalHospitalization;
        }

        public String getSearchedDate() {
            return searchedDate;
        }

        public int getTotalCases() {
            return totalCases;
        }

        public int getTotalFatalities() {
            return totalFatalities;
        }

        public int getTotalVaccinations() {
            return totalVaccinations;
        }

        public int getTotalRecoveries() {
            return totalRecoveries;
        }

        public int getTotalHospitalization(){
            return totalHospitalization;
        }
    }















}

