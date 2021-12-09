package algonquin.cst2335.finalprojectassignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwlSearchFragment extends Fragment implements OwlOnClick {
    View view;
    EditText editText;
    Button button;
    List<String> list;
    RecyclerView recyclerView;
    OwlSearchAdapater searchAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owl_fragement_search, container, false);

        getActivity().setTitle("Search");

        editText = view.findViewById(R.id.editText);
        button = view.findViewById(R.id.button);
        recyclerView = view.findViewById(R.id.recyclerView);

        if (getList() == null)
            list = new ArrayList<>();
        else
            list = getList();
        Collections.reverse(list);
        searchAdapter = new OwlSearchAdapater(getContext(), list, this::onClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(searchAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Please enter text.", Snackbar.LENGTH_LONG).show();
                } else {
                    saveText(text);
                    Fragment fragment = new OwlResultFragement();
                    Bundle bundle = new Bundle();
                    bundle.putString("text", text);
                    fragment.setArguments(bundle);
                    menuItemSelected(fragment);
                    Toast.makeText(getContext(), "Search " + text + " from dictionary.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (searchAdapter != null)
            searchAdapter.reload(getList());
    }

    public SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences("finalProjectAssignment", Context.MODE_PRIVATE);
    }

    public List<String> getList() {
        String json = getSharedPreferences().getString("List", "");

        Type type = new TypeToken<List<String>>() {
        }.getType();

        return new Gson().fromJson(json, type);
    }


    public void saveText(String text) {
        List<String> stringList;
        if (getList() == null)
            stringList = new ArrayList<>();
        else
            stringList = getList();

        stringList.add(text);
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString("List", new Gson().toJson(stringList));
        editor.apply();
    }

    public void menuItemSelected(Fragment fragment) {
        editText.setText("");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(String text) {
        editText.setText(text);
    }
}

