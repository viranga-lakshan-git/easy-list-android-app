package lk.sliit.easylist.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lk.sliit.easylist.R;
import lk.sliit.easylist.layout.Login;
import lk.sliit.easylist.module.Buyer;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerProfileFragment extends Fragment {
    private EditText txtName, txtEmail, txtAddress, txtMobile;
    private TextView txview, txtview_email;

    public BuyerProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_buyer_profile, container, false);

        txtName=view.findViewById(R.id.et_buyer_name);
        txtEmail=view.findViewById(R.id.et_buyer_email);
        txtAddress=view.findViewById(R.id.et_buyer_address);
        txtMobile=view.findViewById(R.id.et_buyer_mobile);
        txview = view.findViewById(R.id.txtxprofilename_text);
        txtview_email = view.findViewById(R.id.txtxemail_txt);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Button btnLogout = view.findViewById(R.id.btn_logout);

        DatabaseReference dbRefBuyerDetails= FirebaseDatabase.getInstance().getReference("Buyer").child(firebaseAuth.getCurrentUser().getUid());
        dbRefBuyerDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Buyer buyer=dataSnapshot.getValue(Buyer.class);
                txtName.setText(buyer.getName());
                txtEmail.setText(buyer.getEmail());
                txtAddress.setText(buyer.getAddress());
                txtMobile.setText(""+buyer.getMobileNumber());
                txview.setText(buyer.getName());
                txtview_email.setText(buyer.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), Login.class));
            }
        });

        return view;
    }
}
