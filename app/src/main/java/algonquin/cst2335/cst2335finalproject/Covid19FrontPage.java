package algonquin.cst2335.cst2335finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private String iconName;
    Bitmap image = null;
    ArrayList<CovidInfo> myCovidInfo;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19_front_page);
        sharedpreferences = getSharedPreferences("CovidPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        EditText searchBar = (EditText) findViewById(R.id.searchBar);
        covidRecycleView = findViewById(R.id.covidRecycleView);
        Button snkBr = findViewById(R.id.snkBar);
        myCovidInfo = new ArrayList<>();
        snkBr.setOnClickListener(clk -> {
            searchItem = searchBar.getText().toString();
            Context context = getApplicationContext();
            Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show();
            editor.putString("searchedItem", searchItem);
            editor.apply();
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

                    for (int i = 0; i < covidArray.length(); i++) {

                        String name = theDocument.getString("province");

                        JSONObject objAtI = covidArray.getJSONObject(i);

                        int totalVaccinations = objAtI.getInt("total_vaccinations");
                        int totalRecoveries = objAtI.getInt("total_recoveries");
                        int totalFatalities = objAtI.getInt("total_fatalities");
                        int totalCases = objAtI.getInt("total_cases");
                        String daydate = objAtI.getString("date");
                        myCovidInfo.add(new CovidInfo(daydate,totalCases,totalFatalities,totalVaccinations,totalRecoveries));





                    /*     File file = new File(getFilesDir(),iconName +".png");
                         if(file.exists()){
                             image = BitmapFactory.decodeFile(getFilesDir()+"/"+ iconName +".png");
                         }else{
                    URL imgUrl = new URL( "https://openweathermap.org/img/w/" + iconName + ".png" );
                    HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                    }
                    FileOutputStream fOut = null;
                    try {
                        fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    }}
                        runOnUiThread(() -> {

                            TextView tv = findViewById(R.id.temp);
                            tv.setText("The current temperature is " + current);
                            tv.setVisibility(View.VISIBLE);
                            tv = findViewById(R.id.minTemp);
                            tv.setText("The maximum temperature is " + max);
                            tv.setVisibility(View.VISIBLE);
                            tv = findViewById(R.id.maxTemp);
                            tv.setText("The min temperature is " + min);
                            tv.setVisibility(View.VISIBLE);
                            tv = findViewById(R.id.humidity);
                            tv.setText("The humidity is " + humidity);
                            tv.setVisibility(View.VISIBLE);
                            tv = findViewById(R.id.description);
                            tv.setText("Description: " + description);
                            tv.setVisibility(View.VISIBLE);


                        });*/
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


    private class CovidInfo{


        String searchedDate;
        int totalCases;
        int totalFatalities;

        int totalVaccinations;
        int totalRecoveries;

        public CovidInfo(String searchedDate ,int totalCases, int totalFatalities, int totalVaccinations, int totalRecoveries) {

            this.searchedDate = searchedDate;
            this.totalCases = totalCases;
            this.totalFatalities = totalFatalities;

            this.totalVaccinations = totalVaccinations;
            this.totalRecoveries = totalRecoveries;
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
    }















    }

