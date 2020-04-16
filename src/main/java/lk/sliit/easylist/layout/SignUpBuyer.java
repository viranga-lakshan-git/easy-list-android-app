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
import lk.sliit.easylist.module.Buyer;

public class SignUpBuyer extends AppCompatActivity {
    EditText txtName,txtEmail,txtMobileNo,txtAddress,txtPassword,txtConfirmPassword;
    Button btnNext;
    Buyer buyer;
    DatabaseReference dbReffBuyer;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    long maxIdBuyer=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_buyer);
        txtName=findViewById(R.id.txtBuyerName);
        txtEmail=findViewById(R.id.txtBuyerEmail);
        txtMobileNo=findViewById(R.id.txtBuyerMobile);
        txtAddress=findViewById(R.id.txtBuyerAddress);
        txtPassword=findViewById(R.id.txtBuyerPassword);
        txtConfirmPassword=findViewById(R.id.txtBuyerConfirmPassowrd);

        btnNext=findViewById(R.id.btnBuyerNext);

        dbReffBuyer= FirebaseDatabase.getInstance().getReference().child("Buyer");
        dbReffBuyer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxIdBuyer=(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name=txtName.getText().toString().trim();
                String email=txtEmail.getText().toString().trim();
                int mobile=Integer.parseInt(txtMobileNo.getText().toString().trim());
                String address=txtAddress.getText().toString().trim();
                String pw=txtPassword.getText().toString().trim();
                String cpw=txtConfirmPassword.getText().toString().trim();

                buyer=new Buyer(name,email,mobile,address);
                dbReffBuyer.child(String.valueOf(maxIdBuyer+1)).setValue(buyer);

                firebaseAuth.createUserWithEmailAndPassword(email,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SignUpBuyer.this, "Registered Successfully. Check Email for the Verification.",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SignUpBuyer.this, Login.class));
                                }
                            });
                        }else{
                            Toast.makeText(SignUpBuyer.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}