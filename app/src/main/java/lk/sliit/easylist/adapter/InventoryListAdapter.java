package lk.sliit.easylist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.module.InventoryItem;
import lk.sliit.easylist.viewholder.InventoryListViewHolder;

public class InventoryListAdapter extends RecyclerView.Adapter<InventoryListViewHolder>  {
    private Context context;
    private ArrayList<InventoryItem> items;

    public InventoryListAdapter(Context c , ArrayList<InventoryItem> i){
        context=c;
        items=i;
    }

    @NonNull
    @Override
    public InventoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryListViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_inventory_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryListViewHolder holder, int position) {
        Picasso.get().load(items.get(position).getImgUrl()).into(holder.imgItem);
        holder.txtItemName.setText(items.get(position).getItemName());
        holder.txtStock.setText(""+items.get(position).getQuantity());
        holder.txtPrice.setText(""+items.get(position).getUnitPrice());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
