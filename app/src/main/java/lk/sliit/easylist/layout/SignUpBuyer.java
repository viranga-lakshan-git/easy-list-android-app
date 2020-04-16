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
import lk.sliit.easylist.module.Buyer;
import lk.sliit.easylist.module.User;

public class SignUpBuyer extends AppCompatActivity {
    EditText txtName,txtEmail,txtMobileNo,txtAddress,txtPassword,txtConfirmPassword;
    Button btnNext;
    Buyer buyer;
    User user;
    DatabaseReference dbReffBuyer,dbReffUser;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_buyer);
        txtName=findViewById(R.id.txtBuyerName);
        txtEmail=findViewById(R.id.txtBuyerEmail);
        txtMobileNo=findViewById(R.id.txtBuyerMobile);
        txtAddress=findViewById(R.id.txtBuyerAddress);
        txtPassword=findViewById(R.id.txtBuyerPassword);
        txtConfirmPassword=findViewById(R.id.txtBuyerConfirmPassowrd);

        btnNext=findViewById(R.id.btnBuyerNext);

        dbReffBuyer= FirebaseDatabase.getInstance().getReference().child("Buyer");
        dbReffUser= FirebaseDatabase.getInstance().getReference().child("User");

        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final String name=txtName.getText().toString().trim();
                final String email=txtEmail.getText().toString().trim();
                final int mobile=Integer.parseInt(txtMobileNo.getText().toString().trim());
                final String address=txtAddress.getText().toString().trim();
                final String pw=txtPassword.getText().toString().trim();
                final String cpw=txtConfirmPassword.getText().toString().trim();

                if(pw.equals(cpw)){
                    firebaseAuth.createUserWithEmailAndPassword(email,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        buyer=new Buyer(name,email,mobile,address);
                                        user=new User("buyer");
                                        dbReffBuyer.child(firebaseAuth.getCurrentUser().getUid()).setValue(buyer);
                                        dbReffUser.child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                                        Toast.makeText(SignUpBuyer.this, "Registered Successfully. Check Email for the Verification.",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignUpBuyer.this, Login.class));
                                    }
                                });
                            }else{
                                Toast.makeText(SignUpBuyer.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(SignUpBuyer.this, "Passwords are not matching. Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}