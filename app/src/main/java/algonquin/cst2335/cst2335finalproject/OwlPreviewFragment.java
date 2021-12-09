package algonquin.cst2335.finalprojectassignment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class OwlPreviewFragment extends Fragment {
    View view;
    Button button;
    TextView textViewType, textViewDefinition, textViewExample;
    ImageView imageView;
    OwlBotDefinition definition;
    CustomDatabase customDatabase;
    long id;
    boolean isSave = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owl_fragement_preview, container, false);

        getActivity().setTitle("Preview");
        closeKeyboard(getActivity());

        customDatabase = new CustomDatabase(getContext());

        button = view.findViewById(R.id.button);
        textViewType = view.findViewById(R.id.textViewType);
        textViewDefinition = view.findViewById(R.id.textViewDefinition);
        textViewExample = view.findViewById(R.id.textViewExample);
        imageView = view.findViewById(R.id.imageView);

        definition = getArguments().getParcelable("definition");

        if (customDatabase.read(definition.getDefinition()) == -1) {
            button.setText("Add");
            isSave = false;
        } else {
            id = customDatabase.read(definition.getDefinition());
            button.setText("Remove");
            isSave = true;
        }

        textViewType.setText(getString(R.string.type, definition.getType()));
        textViewDefinition.setText(getString(R.string.definition, definition.getDefinition()));
        textViewExample.setText(getString(R.string.example, definition.getExample()));

        if (definition.getImageUrl() == null) {
            if (definition.getEmoji() != null) {
                Glide.with(getContext()).load(definition.getEmoji()).into(imageView);
            }
        } else {
            Glide.with(getContext()).load(definition.getImageUrl()).into(imageView);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSave) {
                    isSave = false;
                    customDatabase.remove(id);
                    button.setText("Add");
                } else {
                    isSave = true;
                    id = customDatabase.save(definition.getDefinition());
                    button.setText("Remove");
                }
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

