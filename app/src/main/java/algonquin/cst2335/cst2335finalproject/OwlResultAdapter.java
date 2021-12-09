package algonquin.cst2335.finalprojectassignment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OwlResultAdapter extends RecyclerView.Adapter<OwlResultAdapter.Holder> {
    Context context;
    List<OwlBotDefinition> definitionList;

    public OwlResultAdapter(Context context, List<OwlBotDefinition> definitionList) {
        this.context = context;
        this.definitionList = definitionList;
    }

    @NonNull
    @Override
    public OwlResultAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.owl_item_result, parent, false);
        return new OwlResultAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwlResultAdapter.Holder holder, int position) {
        holder.textView.setText(definitionList.get(position).getDefinition());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OwlPreviewFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("definition", definitionList.get(position));
                fragment.setArguments(bundle);
                menuItemSelected(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return definitionList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    public void menuItemSelected(Fragment fragment) {
        FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

}

