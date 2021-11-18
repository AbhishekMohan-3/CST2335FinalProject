package algonquin.cst2335.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnCovid19 = findViewById(R.id.btnCovid);
        btnCovid19.setOnClickListener(clk ->{
            Intent covidPage = new Intent( MainActivity.this, Covid19FrontPage.class);
            startActivity(covidPage);

        });
    }
}