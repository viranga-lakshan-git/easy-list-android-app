package lk.sliit.easylist.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import lk.sliit.easylist.R;


public class SignUpAs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_as);
    }

    public void btnSignUpAsSellerOnClick(View view) {
        startActivity(new Intent(SignUpAs.this, SignUpSeller.class));
    }

    public void btnSignUpAsBuyerOnClick(View view) {
        startActivity(new Intent(SignUpAs.this, SignUpBuyer.class));
    }
}