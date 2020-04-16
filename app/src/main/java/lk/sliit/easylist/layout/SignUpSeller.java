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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lk.sliit.easylist.R;
import lk.sliit.easylist.module.Seller;
import lk.sliit.easylist.module.User;

public class SignUpSeller extends AppCompatActivity {
    EditText txtName,txtEmail,txtMobileNo,txtShopName, txtShopAddress,txtPassword,txtConfirmPassword;
    Button btnNext, btnSetLocatoin;
    Seller seller;
    User user;
    double lat,lng;
    DatabaseReference dbReffSeller,getDbReffUser;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_seller);

        Bundle extras = getIntent().getExtras();
        lat=extras.getDouble("LAT");
        lng=extras.getDouble("LNG");

        Toast.makeText(SignUpSeller.this, "LAT:"+lat+" LNG:"+lng, Toast.LENGTH_LONG).show();

        txtName=findViewById(R.id.txtSellerName);
        txtEmail=findViewById(R.id.txtSellerEmail);
        txtMobileNo=findViewById(R.id.txtSellerMobile);
        txtShopName=findViewById(R.id.txtShopName);
        txtShopAddress=findViewById(R.id.txtSellerAddress);
        txtPassword=findViewById(R.id.txtSellerPassword);
        txtConfirmPassword=findViewById(R.id.txtSellerConfirmPassword);
        btnNext=findViewById(R.id.btnSellerNext);
        btnSetLocatoin=findViewById(R.id.btnSetLocation);

        dbReffSeller= FirebaseDatabase.getInstance().getReference().child("Seller");
        getDbReffUser= FirebaseDatabase.getInstance().getReference().child("User");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name=txtName.getText().toString();
                final String email=txtEmail.getText().toString();
                final int mobile=Integer.parseInt(txtMobileNo.getText().toString());
                final String shopName=txtShopName.getText().toString();
                final String shopAdd=txtShopAddress.getText().toString();
                final String pw=txtPassword.getText().toString();
                final String cpw=txtConfirmPassword.getText().toString();

                if(pw.equals(cpw)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        seller = new Seller(name, email, mobile, shopName, shopAdd, lat, lng);
                                        user = new User("seller");
                                        dbReffSeller.child(firebaseAuth.getCurrentUser().getUid()).setValue(seller);
                                        getDbReffUser.child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                                        Toast.makeText(SignUpSeller.this, "Registered Successfully. Check Email for the Verification.", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignUpSeller.this, Login.class));
                                    }
                                });
                            } else {
                                Toast.makeText(SignUpSeller.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(SignUpSeller.this, "Passwords are not matching. Try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSetLocatoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpSeller.this, MapSetSellerLocation.class);
                startActivityForResult(intent, 10);
            }
        });


    }

}