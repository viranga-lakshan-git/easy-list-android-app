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
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.adapter.OrderAdapter;
import lk.sliit.easylist.module.ListItem;
import lk.sliit.easylist.module.OrderDetails;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {
    private String userKey;
    private String requestedBuyerKey;
    private String status="requested";

    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_orders, container, false);

        String userType = getActivity().getIntent().getStringExtra("USER_TYPE");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userKey= firebaseAuth.getCurrentUser().getUid();

        Button btnRequested = view.findViewById(R.id.btnRequested);
        Button btnAccepted = view.findViewById(R.id.btnAccepted);
        Button btnReady = view.findViewById(R.id.btnReady);
        Button btnCompleted = view.findViewById(R.id.btnCompleted);
        final RecyclerView recViewOrders= view.findViewById(R.id.recylerview_orders);

        btnRequested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="requested";
            }
        });
        btnAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="requested";
            }
        });
        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="requested";
            }
        });
        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="requested";
            }
        });

        if(userType.equals("seller")){
            DatabaseReference dbRefReciveOrders= FirebaseDatabase.getInstance().getReference("Orders");
            dbRefReciveOrders.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {                                      //get requested buyer's id key
                    for (final DataSnapshot order : dataSnapshot.getChildren()) {
                        requestedBuyerKey = order.getKey();
                    }
                    DatabaseReference dbRefRequestedBuyer = FirebaseDatabase.getInstance().getReference("Buyer").child(requestedBuyerKey);
                    dbRefRequestedBuyer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {                               //get buyer's details
                            final String name = dataSnapshot.child("name").getValue(String.class);
                            final String address = dataSnapshot.child("address").getValue(String.class);
                            final String mobile = dataSnapshot.child("mobileNumber").getValue().toString();
                            final String email = dataSnapshot.child("email").getValue(String.class);
                            // got buyer's details

                            DatabaseReference dbRefOrderDetails = FirebaseDatabase.getInstance().getReference("Orders").child(requestedBuyerKey);
                            dbRefOrderDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final ArrayList<OrderDetails> orderDetailsArrayList=new ArrayList<>();
                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                        if(ds.child("selectedSeller").getValue().toString().equals(userKey)) {   // filtering specific buyer's requests

                                            String price = ds.child("price").getValue().toString();
                                            ArrayList<ListItem> foundItems = new ArrayList<>();
                                            for (DataSnapshot ds2 : ds.child("foundItems").getChildren()) {
                                                ListItem listItem = ds2.getValue(ListItem.class);
                                                foundItems.add(listItem);
                                            }

                                            OrderDetails o = new OrderDetails(name, address, mobile, email, price, foundItems);
                                            orderDetailsArrayList.add(o);
                                        }
                                    }

                                    recViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
                                    OrderAdapter orderAdapter= new OrderAdapter(getContext(),orderDetailsArrayList);
                                    recViewOrders.setAdapter(orderAdapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return view;
    }

}
