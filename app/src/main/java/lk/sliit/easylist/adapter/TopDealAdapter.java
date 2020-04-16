package lk.sliit.easylist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.module.Item;
import lk.sliit.easylist.viewholder.TopDealViewHolder;

public class TopDealAdapter extends RecyclerView.Adapter<TopDealViewHolder> {

    private Context context;
    private ArrayList<Item> items;

    public TopDealAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public TopDealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopDealViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_image_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopDealViewHolder holder, int position) {
        Item i= items.get(position);

        holder.textViewName.setText(i.getItemName());
        Picasso.get().load(items.get(position).getImgURL()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
