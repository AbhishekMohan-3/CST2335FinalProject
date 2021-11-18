package algonquin.cst2335.cst2335finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class Covid19FrontPage extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19_front_page);

        Button searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(v -> {

            Context context = getApplicationContext();
            Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show();
            setdata();
        });
        Button snackBar = findViewById(R.id.snkBar);
        snackBar.setOnClickListener(clk -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Covid19FrontPage.this);
            builder.setMessage("Do you want to implement more functions?")
                    .setTitle("Question:")
                    .setNegativeButton("No", ((dialog, cl) -> {
                    }))
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        Snackbar.make(searchIcon, "Message", Snackbar.LENGTH_SHORT)
                                .setAction("hey", click -> {
                                 EditText searchBar = findViewById(R.id.searchBar);
                                 searchBar.setText("Yes we will!");
                                })
                                .show();
                    })
                    .create().show();
        });
    };



    private int setdata() {

        sharedpreferences = getSharedPreferences("CarChargingPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        EditText searchBar = (EditText) findViewById(R.id.searchBar);
        String searchedItem = searchBar.getText().toString();


       // if (TextUtils.isEmpty(searchedItem)) {
            searchedItem = sharedpreferences.getString("searchedItem", "2222");

        //} else {
            editor.putString("searchedItem", searchedItem);

        //}
            editor.apply();

            searchBar.setText(searchedItem);

            return 1;
        }
    }
