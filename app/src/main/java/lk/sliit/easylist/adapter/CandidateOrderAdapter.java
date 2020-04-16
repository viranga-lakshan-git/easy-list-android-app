package lk.sliit.easylist.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.layout.MapViewCandidateSellers;
import lk.sliit.easylist.module.BillItem;
import lk.sliit.easylist.module.ListItem;
import lk.sliit.easylist.module.Order;
import lk.sliit.easylist.viewholder.CandidateOrderViewHolder;

public class CandidateOrderAdapter extends RecyclerView.Adapter<CandidateOrderViewHolder> {
    private Context context;
    private ArrayList<Order> orders;
    private TextView txtSellerName, txtShopName, txtEmail, txtContact, txtShopAddress, txtPercentage, txtPrice, txtFoundItems, txtNotFoundItems;
    private ArrayList<ListItem> foundListItems, notfoundListItems;
    private Button btnViewSeller;

    public CandidateOrderAdapter(Context c, ArrayList<Order> orders, TextView txtSellerName,TextView txtShopName,TextView txtEmail,TextView txtContact,TextView txtShopAddress,TextView txtPercentage,TextView txtPrice,TextView txtFoundItems,TextView txtNotFoundItems, Button btnViewSellerLocation){
        context=c;
        this.orders=orders;
        this.txtSellerName=txtSellerName;
        this.txtShopName=txtShopName;
        this.txtEmail=txtEmail;
        this.txtContact=txtContact;
        this.txtShopAddress=txtShopAddress;
        this.txtPercentage=txtPercentage;
        this.txtPrice=txtPrice;
        this.txtFoundItems=txtFoundItems;
        this.txtNotFoundItems=txtNotFoundItems;
        this.btnViewSeller=btnViewSellerLocation;
    }

    @NonNull
    @Override
    public CandidateOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CandidateOrderViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_candidate_order,parent,false));
    }

    @Override
    public void onBindViewHolder(CandidateOrderViewHolder holder, final int position) {
        holder.txtShopName.setText(orders.get(position).getSeller().getShopName());
        holder.txtPrice.setText("" + orders.get(position).getTotalPrice() + " Rs.");
        holder.txtPercentage.setText(String.format("%.2f", orders.get(position).getPercentage()) + "%");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSellerName.setText(orders.get(position).getSeller().getName());
                txtShopName.setText(orders.get(position).getSeller().getShopName());
                txtEmail.setText(orders.get(position).getSeller().getEmail());
                txtContact.setText(""+orders.get(position).getSeller().getMobileNumber());
                txtShopAddress.setText(orders.get(position).getSeller().getShopAddress());
                txtPercentage.setText(String.format("%.2f", orders.get(position).getPercentage()) + "%");
                txtPrice.setText(""+orders.get(position).getTotalPrice());

                foundListItems=orders.get(position).getFoundListItems();
                String orderFoundItemList="";
                for (ListItem l1 : foundListItems){
                    orderFoundItemList+=""+l1.getItemName()+": "+l1.getAmount()+"\n";
                }
                txtFoundItems.setText(orderFoundItemList);

                notfoundListItems=orders.get(position).getNotfoundListItems();
                String orderNotFoundItemList="";
                for (ListItem l2 : notfoundListItems){
                    orderNotFoundItemList+=""+l2.getItemName()+": "+l2.getAmount()+"\n";
                }
                txtNotFoundItems.setText(orderNotFoundItemList);

                btnViewSeller.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, MapViewCandidateSellers.class);
                        double lat=orders.get(position).getSeller().getLat();
                        double lng=orders.get(position).getSeller().getLng();
                        intent.putExtra("LAT",lat);
                        intent.putExtra("LNG",lng);
                        intent.putExtra("SHOP_NAME",orders.get(position).getSeller().getShopName());

                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("CANDIDATE_ORDERS", orders);
                        intent.putExtras(bundle);

                        context.startActivity(intent);
                    }
                });
            }
        });

        holder.btnReqOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> itemList = new ArrayList<>();
                for (ListItem listItem : orders.get(position).getFoundListItems()) {
                    itemList.add(listItem.getItemName());
                }
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Bill");
                String id = dbRef.push().getKey();
                BillItem billItem = new BillItem(id, itemList);
                dbRef.child(id).setValue(billItem);

                Query q = FirebaseDatabase.getInstance().getReference().child("Seller").orderByChild("email").equalTo(orders.get(position).getSeller().getEmail());
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String sellerKey=ds.getKey();
                            FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                            DatabaseReference dbRefOrders = FirebaseDatabase.getInstance().getReference("Orders").child(firebaseAuth.getCurrentUser().getUid());
                            String key = dbRefOrders.push().getKey();

                            dbRefOrders.child(key).child("selectedSeller").setValue(sellerKey);
                            dbRefOrders.child(key).child("foundItems").setValue(orders.get(position).getFoundListItems());
                            dbRefOrders.child(key).child("notFoundItems").setValue(orders.get(position).getNotfoundListItems());
                            dbRefOrders.child(key).child("percentage").setValue(orders.get(position).getPercentage());
                            dbRefOrders.child(key).child("price").setValue(orders.get(position).getTotalPrice());
                            dbRefOrders.child(key).child("status").setValue("requested");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
