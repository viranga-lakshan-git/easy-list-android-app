package lk.sliit.easylist.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.fragment.HomeFragment;
import lk.sliit.easylist.module.Item;
import lk.sliit.easylist.module.List;
import lk.sliit.easylist.module.ListItem;
import lk.sliit.easylist.viewholder.ItemViewHolder;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private Context context;
    private ArrayList<Item> items;
    private ArrayList<ListItem> listItems;
    public List selectedList;
    DatabaseReference buyerLists;
    FirebaseAuth firebaseAuth;

    public ItemAdapter(Context c , ArrayList<Item> i, List selectedList){
        context=c;
        items=i;
        this.selectedList=selectedList;
    }

    public List getSelectedList() {
        return selectedList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder,final int position) {
        firebaseAuth=FirebaseAuth.getInstance();
        holder.itemName.setText(items.get(position).getItemName());
        Picasso.get().load(items.get(position).getImgURL()).into(holder.itemImage);
        holder.etAmount.setText(String.format("%d",1));
        holder.btnIncAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.etAmount.setText(String.format("%d",Integer.parseInt(holder.etAmount.getText().toString())+1));
            }
        });

        holder.btnDecAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.etAmount.setText(String.format("%d",Integer.parseInt(holder.etAmount.getText().toString())-1));
            }
        });

        holder.btnAddToList.setOnClickListener(new View.OnClickListener() {
            ListItem listItem;
            List selectedList = getSelectedList();
            String itemName =items.get(position).getItemName();
            @Override
            public void onClick(View v) {

                buyerLists=FirebaseDatabase.getInstance().getReference("Buyer").child(firebaseAuth.getCurrentUser().getUid()).child("Lists");

                listItem = new ListItem(items.get(position).getItemName(),Integer.parseInt(holder.etAmount.getText().toString()));
                buyerLists.child(selectedList.getListType()).child(selectedList.getListName()).child("Items").child(itemName).setValue(listItem);
                Toast.makeText(context, listItem.getItemName()+" added to "+selectedList.getListType()+": "+selectedList.getListName(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}