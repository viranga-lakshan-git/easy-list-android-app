package lk.sliit.easylist.layout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import lk.sliit.easylist.R;
import lk.sliit.easylist.function.AprioriFrequentItemsetGenerator;
import lk.sliit.easylist.function.FrequentItemsetData;
import lk.sliit.easylist.function.Resultset;

public class Login extends AppCompatActivity {
    TextView tvEnglish, tvSinhala, tvTamil;
    EditText txtUsername, txtPassword;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference userDBRef;
    Button btnLogin;
    java.util.List<ArrayList> list = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_login);

        tvEnglish=findViewById(R.id.tvEnglish);
        tvSinhala=findViewById(R.id.tvSinhala);
        tvTamil=findViewById(R.id.tvTamil);

        tvSinhala.setText("සිංහල");
        tvTamil.setText("Tamil");
        tvEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
                recreate();
            }
        });

        tvSinhala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("si");
                recreate();
            }
        });

        tvTamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Tamil Language Support isn't available yet.", Toast.LENGTH_LONG).show();
            }
        });

        txtUsername = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setEnabled(false);
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                final java.util.List<Resultset> gotResultlist =Datamining();

                firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                userDBRef=FirebaseDatabase.getInstance().getReference().child("User").child(firebaseAuth.getCurrentUser().getUid());
                                userDBRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Intent i = new Intent(Login.this, MainActivity.class);
                                        if(Objects.requireNonNull(dataSnapshot.child("userType").getValue()).toString().equals("buyer")){
                                            i.putExtra("USER_TYPE", "buyer");
                                            i.putExtra("selectedList", (Serializable) gotResultlist);
                                        }else {
                                            i.putExtra("USER_TYPE", "seller");
                                            i.putExtra("selectedList", (Serializable) gotResultlist);
                                        }
                                        startActivity(i);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else {
                                Toast.makeText(Login.this, "Please verify your Email address.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        DatabaseReference dbRefBills=FirebaseDatabase.getInstance().getReference("Bill");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ArrayList<String> items = new ArrayList<>();

                    for (int i = 0; i < ds.child("items").getChildrenCount(); i++) {
                        String name = ds.child("items").child("" + i).getValue(String.class);
                        items.add(name);
                    }
                    list.add(items);
                }
                System.out.println("size" + list.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dbRefBills.addListenerForSingleValueEvent(eventListener);

    }

    public void btnRegisterOnClick(View view) {

        startActivity(new Intent(Login.this, SignUpAs.class));
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs= getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language= prefs.getString("My_Lang", "");
        setLocale(language);
    }

    public java.util.List<Resultset> Datamining(){
        int i, p;
        java.util.List<Resultset> Resultlist = new ArrayList<>();
        java.util.List<Set<String>> itemsetList = new ArrayList<>();;
        for (i = 0; i < list.size(); i++) {
            int itemcount = list.get(i).size();
            String[] array = new String[itemcount];
            for (p = 0; p < list.get(i).size(); p++) {
                array[p] = list.get(i).get(p).toString();
            }
            itemsetList.add(new HashSet<>(Arrays.asList(array)));
        }

        AprioriFrequentItemsetGenerator<String> generator = new AprioriFrequentItemsetGenerator<>();
        FrequentItemsetData<String> data = generator.generate(itemsetList, 0.2);

        int k = 1;
        for (Set<String> itemset : data.getFrequentItemsetList()) {
            k++;
            String item = itemset.toString();

            if (!item.contains(",")){
                float sup = (float)data.getSupport(itemset);
                Resultset rs = new Resultset(item,sup);
                Resultlist.add(rs);
            }
            Collections.sort(Resultlist);
        }
        System.out.println("finish");
        return Resultlist;
    }
}