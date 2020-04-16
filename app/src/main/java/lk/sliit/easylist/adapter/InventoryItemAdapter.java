package lk.sliit.easylist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.module.InventoryItem;
import lk.sliit.easylist.module.Item;
import lk.sliit.easylist.viewholder.InventoryItemViewHolder;

public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemViewHolder> {
    private Context context;
    private ArrayList<Item> items;
    private String buyerID;
    private DatabaseReference databaseReference;

    public InventoryItemAdapter(Context c , ArrayList<Item> i, String key){
        context=c;
        items=i;
        buyerID =key;
        databaseReference=FirebaseDatabase.getInstance().getReference("Seller").child(buyerID).child("Inventory");
    }

    @NonNull
    @Override
    public InventoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryItemViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_inventory_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final InventoryItemViewHolder holder, final int position) {
        holder.itemName.setText(items.get(position).getItemName());
        Picasso.get().load(items.get(position).getImgURL()).into(holder.itemImage);
        holder.btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.etQuantity.getText().toString().isEmpty())holder.etQuantity.setText(""+1);
                else holder.etQuantity.setText(""+(Float.parseFloat(holder.etQuantity.getText().toString())+1));
            }
        });

        holder.btnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.etQuantity.getText().equals(""))holder.etQuantity.setText(""+1);
                else holder.etQuantity.setText(""+(Float.parseFloat(holder.etQuantity.getText().toString())-1));
            }
        });

        holder.btnIncPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.etPrice.getText().toString().isEmpty())holder.etPrice.setText(""+1);
                else holder.etPrice.setText(""+(Float.parseFloat(holder.etPrice.getText().toString())+1));
            }
        });

        holder.btnDecPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.etPrice.getText().toString().isEmpty())holder.etPrice.setText(""+1);
                else holder.etPrice.setText(""+(Float.parseFloat(holder.etPrice.getText().toString())-1));
            }
        });

        holder.btnAddToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InventoryItem inventoryItem = new InventoryItem(items.get(position).getItemName(),
                        items.get(position).getCategory(),
                        items.get(position).getImgURL(),
                        Float.parseFloat(holder.etQuantity.getText().toString()),
                        Float.parseFloat(holder.etPrice.getText().toString()));
                databaseReference.child(items.get(position).getCategory()).child(items.get(position).getItemName()).setValue(inventoryItem);
                Toast.makeText(context, items.get(position).getItemName()+" added to inventory",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}