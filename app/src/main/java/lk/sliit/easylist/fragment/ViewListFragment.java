package lk.sliit.easylist.fragment;


import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.adapter.ListViewAdapter;
import lk.sliit.easylist.module.InventoryItem;
import lk.sliit.easylist.module.ListItem;
import lk.sliit.easylist.module.Order;
import lk.sliit.easylist.module.Seller;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewListFragment extends Fragment {
    private String userKey,userType;
    private String listName,listType;

    private RecyclerView rvListItems;
    private ListViewAdapter listViewAdapter;
    private static final String TAG = "ViewList";

    private Location currentLocation;
    private double lat,lng;

    public ViewListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_list, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userKey= firebaseAuth.getCurrentUser().getUid();
        Button btnSearchA = view.findViewById(R.id.btnSearchAvailability);

        rvListItems=view.findViewById(R.id.rvListContent);
        rvListItems.setLayoutManager(new LinearLayoutManager(getContext()));

        Bundle args = this.getArguments();
        if (args != null) {
            listName= args.getString("LIST_NAME");
            listType= args.getString("LIST_TYPE");
        }

        TextView txtListName = view.findViewById(R.id.tvListName);
        txtListName.setText(listType+" : "+listName);

        System.out.println("userkey: "+userKey+", listtype: "+listType+", listname: "+listName);

        DatabaseReference dbListItems= FirebaseDatabase.getInstance().getReference("Buyer").child(userKey).child("Lists").child(listType).child(listName).child("Items");
        dbListItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<ListItem> listItems=new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ListItem listItem = ds.getValue(ListItem.class);
                    listItems.add(listItem);
                }
                listViewAdapter= new ListViewAdapter(getContext(),listItems);
                rvListItems.setAdapter(listViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Log.d(TAG, "getDeviceLocation: Getting the devices current location");
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        try {
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Found location");
                        currentLocation = (Location) task.getResult();
                    } else {
                        Log.d(TAG, "onComplete: Current location is null");
                        Toast.makeText(getActivity(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }

        btnSearchA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat=6.9120144;//currentLocation.getLatitude();
                lng=79.8519067;//currentLocation.getLongitude();
                FirebaseDatabase.getInstance().getReference("Buyer").child(userKey).child("latitude").setValue(lat);
                FirebaseDatabase.getInstance().getReference("Buyer").child(userKey).child("longitude").setValue(lng);
                Toast.makeText(getContext(), "Searching sellers near to your location", Toast.LENGTH_SHORT).show();

                DatabaseReference dbListItems= FirebaseDatabase.getInstance().getReference("List").child(userKey).child("Lists").child(listType).child(listName).child("Items");
                dbListItems.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final ArrayList<ListItem> listItems=new ArrayList<>();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            ListItem listItem = ds.getValue(ListItem.class);
                            listItems.add(listItem);
                        }

                        DatabaseReference dbRefSellers = FirebaseDatabase.getInstance().getReference("Seller");
                        dbRefSellers.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //getting all sellers
                                final ArrayList<Seller> sellers=new ArrayList<>();
                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    Seller s=ds.getValue(Seller.class);
                                    s.setDs(ds.child("Inventory"));
                                    s.setInventoryItems();
                                    sellers.add(s);
                                }

                                final ArrayList<Seller> nearSellers=new ArrayList<>();
                                for (Seller s : sellers){
                                    if (getDistance(s.getLat(),lat, s.getLng(), lng,0,0)<=2000){              //currentLocation.getLatitude(), currentLocation.getLongitude()
                                        nearSellers.add(s);
                                    }       //selecting sellers near 2kms to current location
                                }

                                //-------------------------------------------------------------------------------------------

                                DatabaseReference dbRefBuyerlist;
                                dbRefBuyerlist= FirebaseDatabase.getInstance().getReference("Buyer").child(userKey).child("Lists").child(listType).child(listName).child("Items");
                                dbRefBuyerlist.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final ArrayList<ListItem> searchingListItems=new ArrayList<>();
                                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                            ListItem listItem = ds.getValue(ListItem.class);
                                            searchingListItems.add(listItem);
                                        }   //getting buyer's list items to listItems Array
                                        ArrayList<Order> candidateOrders = new ArrayList<>();
                                        for (Seller s : nearSellers) {  //going through sellers near to buyer's current location
                                            ArrayList<InventoryItem> inventoryItems = s.getInventoryItems();
                                            ArrayList<ListItem> foundItems = new ArrayList<>();
                                            ArrayList<ListItem> searchingItems = new ArrayList<>();
                                            searchingItems.addAll(searchingListItems);

                                            float foundPercentage;
                                            float price = 0;
                                            for(ListItem searchingListItem : searchingListItems) {    //going through buyer's items in selected searching list
                                                for (InventoryItem inventoryItem : inventoryItems) {    //going through a one seller's inventory
                                                    if (inventoryItem.getItemName().equals(searchingListItem.getItemName()) && inventoryItem.getQuantity()>=searchingListItem.getAmount() ) {
                                                        foundItems.add(searchingListItem); //store found items in foundItems array
                                                        price += (searchingListItem.getAmount()*inventoryItem.getUnitPrice()); //Calculate total price
                                                    }
                                                }
                                            }
                                            float foundCount=foundItems.size(),listSize=searchingListItems.size();
                                            foundPercentage = (foundCount/listSize)*100;
                                            searchingItems.removeAll(foundItems);   //remove found items from searching items
                                            Order order = new Order(userKey, s, foundItems, searchingItems, foundPercentage, price);
                                            candidateOrders.add(order);   //store candidate order details in an array
                                        }

                                        //sending nearest sellers inside orders array to the next map intent
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelableArrayList("CANDIDATE_ORDERS", candidateOrders);
                                        bundle.putString("LIST_NAME",listName);
                                        bundle.putString("LIST_TYPE",listType);
                                        bundle.putString("USER_TYPE",userType);

                                        CandidateSellersFragment candidateSellersFragment=new CandidateSellersFragment();
                                        candidateSellersFragment.setArguments(bundle);

                                        setFragment(candidateSellersFragment);
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
        });

        return view;
    }

    public static double getDistance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
