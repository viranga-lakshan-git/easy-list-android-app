package lk.sliit.easylist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.module.ListItem;
import lk.sliit.easylist.module.OrderDetails;
import lk.sliit.easylist.viewholder.OrderViewHolder;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    public Context context;
    ArrayList<OrderDetails> odList;
    public OrderAdapter(Context c, ArrayList<OrderDetails> od){
        context=c;
        odList=od;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.txtName.setText(odList.get(position).getName());
        holder.txtAddress.setText(odList.get(position).getAddress());
        holder.txtMobile.setText(odList.get(position).getMobile());
        holder.txtEmail.setText(odList.get(position).getEmail());
        holder.txtPrice.setText(odList.get(position).getPrice());
        String found="";
        for(ListItem li : odList.get(position).getFoundItems()){
            found+=li.getItemName()+": "+li.getAmount()+"\n";
        }

        holder.txtFoundItems.setText(found);
    }

    @Override
    public int getItemCount() {
        return odList.size();
    }
}
