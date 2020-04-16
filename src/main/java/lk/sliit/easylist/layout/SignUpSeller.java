package lk.sliit.easylist.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lk.sliit.easylist.R;
import lk.sliit.easylist.module.Seller;

public class SignUpSeller extends AppCompatActivity {
    EditText txtName,txtEmail,txtMobileNo,txtShopName, txtShopAddress,txtPassword,txtConfirmPassword;
    Button btnNext;
    Seller seller;
    DatabaseReference dbReffSeller;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    long maxIdSeller=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_seller);

        txtName=findViewById(R.id.txtSellerName);
        txtEmail=findViewById(R.id.txtSellerEmail);
        txtMobileNo=findViewById(R.id.txtSellerMobile);
        txtShopName=findViewById(R.id.txtShopName);
        txtShopAddress=findViewById(R.id.txtSellerAddress);
        txtPassword=findViewById(R.id.txtSellerPassword);
        txtConfirmPassword=findViewById(R.id.txtSellerConfirmPassword);
        btnNext=findViewById(R.id.btnSellerNext);

        dbReffSeller= FirebaseDatabase.getInstance().getReference().child("Seller");
        dbReffSeller.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxIdSeller=(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=txtName.getText().toString();
                String email=txtEmail.getText().toString();
                int mobile=Integer.parseInt(txtMobileNo.getText().toString());
                String shopName=txtShopName.getText().toString();
                String shopAdd=txtShopAddress.getText().toString();
                String pw=txtPassword.getText().toString();
                String cpw=txtConfirmPassword.getText().toString();

                seller=new Seller(name,email,mobile,shopName,shopAdd);
                dbReffSeller.child(String.valueOf(maxIdSeller+1)).setValue(seller);

                firebaseAuth.createUserWithEmailAndPassword(email,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SignUpSeller.this, "Registered Successfully. Check Email for the Verification.",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SignUpSeller.this, Login.class));
                                }
                            });
                        }else{
                            Toast.makeText(SignUpSeller.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

}