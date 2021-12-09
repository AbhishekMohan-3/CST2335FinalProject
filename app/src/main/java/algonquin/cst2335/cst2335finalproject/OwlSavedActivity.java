package algonquin.cst2335.finalprojectassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class OwlSavedActivity extends AppCompatActivity {

    CustomDatabase customDatabase;
    RecyclerView recyclerView;
    OwlAdapter savedAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owl_saved);

        setTitle("Saved");

        recyclerView = findViewById(R.id.recyclerView);
        customDatabase = new CustomDatabase(OwlSavedActivity.this);
        savedAdapter = new OwlAdapter(OwlSavedActivity.this, customDatabase.readAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(OwlSavedActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(savedAdapter);
    }
}