package algonquin.cst2335.finalprojectassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class OwlSavedAdapter extends RecyclerView.Adapter<OwlSavedAdapter.Holder> {
    Context context;
    HashMap<String, Long> list;

    public OwlSavedAdapter(Context context, HashMap<String, Long> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OwlSavedAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.owl_item_saved, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwlSavedAdapter.Holder holder, int position) {
        holder.textView.setText((String) list.keySet().toArray()[position]);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomDatabase(context).remove(list.get(list.keySet().toArray()[position]));
                list.remove(list.keySet().toArray()[position]);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


}

