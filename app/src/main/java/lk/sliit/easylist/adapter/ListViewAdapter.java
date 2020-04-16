package lk.sliit.easylist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.module.ListItem;
import lk.sliit.easylist.viewholder.ListItemViewHolder;

public class ListViewAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

    public Context context;
    public ArrayList<ListItem> listItems;

    public ListViewAdapter(Context c , ArrayList<ListItem> l){
        context=c;
        listItems=l;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListItemViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_list_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        holder.txtItemName.setText(listItems.get(position).getItemName());
        holder.txtItemQty.setText(""+listItems.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
