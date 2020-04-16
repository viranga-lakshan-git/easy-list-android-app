package lk.sliit.easylist.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;

import lk.sliit.easylist.R;
import lk.sliit.easylist.adapter.InventoryListAdapter;
import lk.sliit.easylist.module.InventoryItem;
import lk.sliit.easylist.module.Item;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment {
    private RecyclerView rvInventoryList;
    private ArrayList<Item> items;
    private ArrayList<InventoryItem> inventoryItems;
    private ArrayList<String> sellerCategories;
    private HashSet<String> uniqueSellerCategories;
    private InventoryListAdapter inventoryListAdapter;

    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_inventory, container, false);

        String userType = getActivity().getIntent().getStringExtra("USER_TYPE");
        super.onCreate(savedInstanceState);

        inventoryItems=new ArrayList<>();
        sellerCategories=new ArrayList<>();
        items=new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference sellerInventory = FirebaseDatabase.getInstance().getReference("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Inventory");
        rvInventoryList = view.findViewById(R.id.recyclerViewInventoryList);
        rvInventoryList.setLayoutManager(new LinearLayoutManager(getContext()));

        sellerInventory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                    for(DataSnapshot ds : d1.getChildren()) {
                        InventoryItem inventoryItem = ds.getValue(InventoryItem.class);
                        sellerCategories.add(inventoryItem.getCategory());
                        inventoryItems.add(inventoryItem);
                    }
                }

                uniqueSellerCategories = new HashSet<>(sellerCategories);

                inventoryListAdapter = new InventoryListAdapter(getContext(), inventoryItems);
                rvInventoryList.setLayoutManager(new LinearLayoutManager(getContext()));
                rvInventoryList.setAdapter(inventoryListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
