package algonquin.cst2335.cst2335finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

/**
 *This class represents the first page of Covid19 Information Tracker.
 * It includes loading data from URl and showing it in a recyclerView, as well as allowing
 * users to save a particular record and delete it as well.
 * @author Abhishek Mohan
 * @version 1.0
 *
 *
 */

public class Covid19FrontPage extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    String searchItem ;
    RecyclerView covidRecycleView;
    private String stringURL;
    ArrayList<CovidInfo> myCovidInfo = new ArrayList<>();
    MyCovidAdapter adt;
    AlertDialog loadDialogue;
    Button searchButton;
    SQLiteDatabase db;




    @Override
    /**
     * this function is initializing the toolbar
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19_front_page);
        Toolbar myToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(myToolbar);
        /**
         *shared preferences to store the last search of the user
         */
        sharedpreferences = getSharedPreferences("CovidPref", Context.MODE_PRIVATE);
        /**
         * Setting up edittext,recyclerView, and using the adapter object on recyclerView
         */
        SharedPreferences.Editor editor = sharedpreferences.edit();
        EditText searchBar = (EditText) findViewById(R.id.searchBar);
        covidRecycleView = findViewById(R.id.covidRecycleView);
        adt = new MyCovidAdapter();
        covidRecycleView.setAdapter(adt);
        covidRecycleView.setLayoutManager(new LinearLayoutManager(this));
        /** creating a database object to store the data*/
        MyCovidHelper opener = new MyCovidHelper( this );
        db =opener.getWritableDatabase();

        /** creating onclickListenere on searchButton*/
        searchButton = findViewById(R.id.snkBar);
       searchButton.setOnClickListener(clk -> {
           /**getting the date searched by user */
            searchItem = searchBar.getText().toString();
            Context context = getApplicationContext();
           /**Toast message to show "please wait" message */
            Toast.makeText(context, "Please wait while data is loading", Toast.LENGTH_SHORT).show();
           /** Editor for shared Preferences*/
            editor.putString("searchedItem", searchItem);
            editor.apply();
           /**AlertDialog to show that data is being fetched */
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Fetching the data after: ")
                    .setMessage(searchItem)
                    .setView( new ProgressBar(this))
                    .show();
           /**Using executor to retrieve data from URl */
            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute( () -> {



                /** Below code is using date searched to grab the data related to that date.
                 *
                 * It consisted to creating JSON Object, and getting data from it's fields.
                 * Then JSON array  is storing the data of all the found records*/
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
                    /** for loop is used to store the information in variables
                     * and all the data is stored in the arrayList*/
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
                            /** data loaded and adapter is notified of all the changes*/
                           // adt.notifyItemInserted(myCovidInfo.size()-1);
                            adt.notifyDataSetChanged();
                            dialog.hide();

                        });


                    }

                }
                /**Try catch block for detecting JSON or IO exceptions*/
                catch (IOException | JSONException ioe){
                    Log.e("Connection Error", ioe.getMessage());
                }
            } );

        });

        /**Shared preferences*/
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


    /**
     *
     * This class is used to create the view of a row in the recyclerView
     * It extends RecyclerView.ViewHolder
     */

    private class CovidRowViews extends RecyclerView.ViewHolder{
        /**TextViews of the row to be displayed*/
        TextView dateData;
        TextView casesData;
        TextView fatalitiesData;
        TextView hospitalData;
        TextView vaccinationData;
        TextView recoveryData;
        int position = 0;
        public CovidRowViews( View itemView) {
            super(itemView);
            /**initializing textViews*/
            dateData = itemView.findViewById(R.id.dateData);
            vaccinationData = itemView.findViewById(R.id.vaccinationData);
            fatalitiesData = itemView.findViewById(R.id.fatalitiesData);
             recoveryData = itemView.findViewById(R.id.recoveryData);
            hospitalData = itemView.findViewById(R.id.hospitalizationData);
            casesData = itemView.findViewById(R.id.caseData);
            /**When user click on a row, it asks using alterDialog if he or she wants to dave the record
             * If yes, the record is saved in the database. */
            itemView.setOnClickListener(click->{
                AlertDialog.Builder builder = new AlertDialog.Builder( Covid19FrontPage.this);
                builder.setMessage("Do you want to save data for the date: " + dateData.getText())
                        .setTitle("Question: ")
                        .setNegativeButton("No", ((dialog, cl) -> {
                        }))
                        .setPositiveButton("Yes",((dialog, cl) -> {
                  //  CovidInfo = new CovidInfo(dateData.getText(),casesData.getText(),fatalitiesData.getText(),hospitalData.getText(),vaccinationData.getText(),recoveryData.getText());
                            /**Getting data of record to be saved and inserting it in the database*/
                            ContentValues savingRow = new ContentValues();
                            savingRow.put(MyCovidHelper.col_date, (String) dateData.getText());
                            savingRow.put(MyCovidHelper.col_cases, (String) casesData.getText());
                            savingRow.put(MyCovidHelper.col_fatalities, (String) fatalitiesData.getText());
                            savingRow.put(MyCovidHelper.col_hospitalizations, (String) hospitalData.getText());
                            savingRow.put(MyCovidHelper.col_vaccinations, (String) vaccinationData.getText());
                            savingRow.put(MyCovidHelper.col_recoveries, (String) recoveryData.getText());
                            db.insert(MyCovidHelper.TABLE_NAME,MyCovidHelper.col_cases,savingRow);

                            /**Informing user that data is saved and to undo saving, click "Undo Saving"*/
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



    /**Adapter Class to be used to display data in recyclerView*/
    public class MyCovidAdapter extends RecyclerView.Adapter<CovidRowViews>{




        @Override
        /**
         *Creates the view for row
         * @param parent: it is the ViewGroup
         * @viewType: used to chose the viewType which is same this time
         * @return Returns the view.
         */
        public CovidRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View loadedRow = inflater.inflate(R.layout.covid_searched_item, parent, false);

            return new CovidRowViews(loadedRow);
        }
        @Override
        /**
         * sets the data in the edit texts of rows
         * @param holder: the holder object
         * @param position : represents the position of the row
         * @return Returns void
         * */
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
        /**It gets the count of items
         * @return size of array*/
        public int getItemCount() {
            return myCovidInfo.size();
        }
        public int getItemViewType(int position) {
            CovidInfo thisRow = myCovidInfo.get(position);
            return super.getItemViewType(position);
        }
    }

    /**CovidInfo class which represents a CovidInfo object that stores the data obtained from
     * the date. It represents the data and its related data*/
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
        /**
         * Returns the CovidInfo object
         * @param searchedDate: the date of the record
         * @param totalCases: total cases of the record
         * @param totalFatalities: total fatalities in the record
         * @param totalHospitalization: total hospitalizations in  the record
         * @param totalRecoveries: total recoveries in the record
         * @param totalVaccinations: total vaccinations in the record
         *
         * */

        public CovidInfo(String searchedDate ,int totalCases, int totalFatalities, int totalVaccinations, int totalRecoveries,int totalHospitalization) {

            this.searchedDate = searchedDate;
            this.totalCases = totalCases;
            this.totalFatalities = totalFatalities;

            this.totalVaccinations = totalVaccinations;
            this.totalRecoveries = totalRecoveries;
            this.totalHospitalization = totalHospitalization;
        }
        /**Getters and setters*/
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





    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.goTo_OwlBot:
                Intent owlBotPage = new Intent( Covid19FrontPage.this, MainActivity.class);
                startActivity(owlBotPage);
                break;
            case R.id.gotTo_Carbon:
               /* Intent carbonPage = new Intent( Covid19FrontPage.this, MainActivity.class);
                startActivity(carbonPage);*/
                break;
            case R.id.helpMenu:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Instructions: ")
                        .setMessage("Type the date you want to enter in the given format and press search button.\n List will be loaded. Click on any record to save.\n If you want to undo save, press undo saving. ")
                        .show();

        }
        return super.onOptionsItemSelected(item);
    }









}

