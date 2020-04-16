package lk.sliit.easylist.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lk.sliit.easylist.R;
import lk.sliit.easylist.layout.Login;
import lk.sliit.easylist.module.Seller;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerProfileFragment extends Fragment {
    private EditText txtName, txtEmail, txtAddress, txtMobile, txtShopName;
    private TextView sellertextview,selleremailview;

    public SellerProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_profile, container, false);

        txtName=view.findViewById(R.id.et_seller_name);
        txtEmail=view.findViewById(R.id.et_seller_email);
        txtAddress=view.findViewById(R.id.et_seller_address);
        txtMobile=view.findViewById(R.id.et_seller_mobile);
        txtShopName=view.findViewById(R.id.et_shop_name);
        sellertextview = view.findViewById(R.id.txtxSellerprofilename);
        selleremailview = view.findViewById(R.id.txtxemail_txt);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference dbRefBuyerDetails= FirebaseDatabase.getInstance().getReference("Seller").child(firebaseAuth.getCurrentUser().getUid());
        dbRefBuyerDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Seller seller=dataSnapshot.getValue(Seller.class);
                txtName.setText(seller.getName());
                txtEmail.setText(seller.getEmail());
                txtAddress.setText(seller.getShopAddress());
                txtMobile.setText(""+seller.getMobileNumber());
                txtShopName.setText(seller.getShopName());
                sellertextview.setText(seller.getName());
                selleremailview.setText(seller.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void btnSignOutSellerOnClick(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), Login.class));
    }

}
