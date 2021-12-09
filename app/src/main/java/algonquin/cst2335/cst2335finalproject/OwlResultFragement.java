package algonquin.cst2335.finalprojectassignment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwlResultFragement extends Fragment {
    View view;
    String text;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    OwlResultAdapter resultAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owl_fragement_result, container, false);

        getActivity().setTitle("Result");
        closeKeyboard(getActivity());

        text = getArguments().getString("text");
        recyclerView = view.findViewById(R.id.recyclerView);

        dialog = ProgressDialog.show(getContext(),"","Loading...",false,false);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<APIResponse> call = apiInterface.getResponse("Token d8c129e219667fc9761a4d0f44fe4e63a3db420f", text);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                Log.d("theS", "onResponse: " + response.isSuccessful()    );
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            resultAdapter = new OwlResultAdapter(getContext(), response.body().getDefinitions());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(resultAdapter);
                        }else{
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Error: "+response.code(), Snackbar.LENGTH_LONG).show();
                        }
                    }else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Response body is null.", Snackbar.LENGTH_LONG).show();
                    }
                }else{
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Response is unSuccessful. "+response.code(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                dialog.dismiss();
                Snackbar.make(getActivity().findViewById(android.R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

        return view;
    }

    public  void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        // To get the correct window token, lets first get the currently focused view
        View view = activity.getCurrentFocus();

        // To get the window token when there is no currently focused view, we have a to create a view
        if (view == null) {
            view = new View(activity);
        }

        // hide the keyboard
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

