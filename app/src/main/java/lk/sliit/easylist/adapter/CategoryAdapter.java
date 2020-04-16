package lk.sliit.easylist.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.module.Category;
import lk.sliit.easylist.module.Item;
import lk.sliit.easylist.module.List;
import lk.sliit.easylist.viewholder.CategoryViewHolder;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>{
    Context context;
    ArrayList<Category> categories;
    ArrayList<Item> itemArrayList;
    ItemAdapter itemAdapter;
    InventoryItemAdapter inventoryItemAdapter;
    RecyclerView recyclerViewItem;
    String userType;
    List selectedList;
    FirebaseAuth firebaseAuth;
    int j;

    public CategoryAdapter(Context c , ArrayList<Category> cat , RecyclerView recyclerViewItem, String userType, List selectedList){
        context=c;
        categories=cat;
        this.recyclerViewItem=recyclerViewItem;
        this.userType=userType;
        this.selectedList=selectedList;
        firebaseAuth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_category,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {

        holder.catName.setText(categories.get(position).getCategoryName());
        Picasso.get().load(categories.get(position).getImgURL()).into(holder.catImage);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemArrayList = new ArrayList<>();
                DatabaseReference databaseReferenceItems = FirebaseDatabase.getInstance().getReference().child("Item").child(categories.get(position).getCategoryName());
                databaseReferenceItems.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        j=0;
                        if(dataSnapshot!=null) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                final Item item = d.getValue(Item.class);
                                StorageReference storageReferenceItems = FirebaseStorage.getInstance().getReference();
                                storageReferenceItems.child(item.getImgURL()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        item.setItemName(item.getItemName());
                                        item.setImgURL(uri.toString());
                                        itemArrayList.add(item);
                                        if (j == dataSnapshot.getChildrenCount()) {
                                            if(userType.equals("buyer")) {
                                                itemAdapter = new ItemAdapter(context, itemArrayList,selectedList);
                                                recyclerViewItem.setLayoutManager(new GridLayoutManager(context, 3));
                                                recyclerViewItem.setAdapter(itemAdapter);
                                            }else{
                                                inventoryItemAdapter = new InventoryItemAdapter(context, itemArrayList, firebaseAuth.getCurrentUser().getUid() );
                                                recyclerViewItem.setLayoutManager(new GridLayoutManager(context, 3));
                                                recyclerViewItem.setAdapter(inventoryItemAdapter);
                                            }
                                        }
                                    }
                                });
                                j++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, "Something went wrong...",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
