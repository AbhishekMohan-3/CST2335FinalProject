package algonquin.cst2335.finalprojectassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwlSearchAdapater extends RecyclerView.Adapter<OwlSearchAdapater.Holder> {
    Context context;
    List<String> list;
    OwlOnClick onClick;

    public OwlSearchAdapater(Context context, List<String> list, OwlOnClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }

    public void reload(List<String> list) {
        this.list.clear();
        if (list == null)
            this.list.addAll(new ArrayList<>());
        else
            this.list.addAll(list);

        Collections.reverse(this.list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OwlSearchAdapater.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.owl_item_search, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwlSearchAdapater.Holder holder, int position) {
        holder.textView.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
