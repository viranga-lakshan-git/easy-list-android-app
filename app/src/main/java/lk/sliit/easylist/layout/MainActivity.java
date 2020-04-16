package lk.sliit.easylist.layout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import lk.sliit.easylist.R;
import lk.sliit.easylist.fragment.AlertsFragment;
import lk.sliit.easylist.fragment.BuyerProfileFragment;
import lk.sliit.easylist.fragment.HomeFragment;
import lk.sliit.easylist.fragment.InventoryFragment;
import lk.sliit.easylist.fragment.ListsFragment;
import lk.sliit.easylist.fragment.OrdersFragment;
import lk.sliit.easylist.fragment.SellerProfileFragment;
import lk.sliit.easylist.module.Buyer;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private HomeFragment homeFragment;
    private ListsFragment listsFragment;
    private OrdersFragment ordersFragment;
    private AlertsFragment alertsFragment;
    private SellerProfileFragment sellerProfileFragment;
    private BuyerProfileFragment buyerProfileFragment;
    private InventoryFragment inventoryFragment;

    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment= new HomeFragment();
        listsFragment = new ListsFragment();
        inventoryFragment = new InventoryFragment();
        ordersFragment = new OrdersFragment();
        alertsFragment = new AlertsFragment();
        sellerProfileFragment = new SellerProfileFragment();
        buyerProfileFragment = new BuyerProfileFragment();

        userType= getIntent().getStringExtra("USER_TYPE");

        bottomNavigationView=findViewById(R.id.bottom_nav);
        frameLayout=findViewById(R.id.main_frame);

        setFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home:
                        setFragment(homeFragment);
                        return true;

                    case R.id.action_lists:
                        if(userType.equals("seller")){
                            setFragment(inventoryFragment);
                            return true;
                        }else{
                            setFragment(listsFragment);
                            return true;
                        }

                    case R.id.action_orders:
                        setFragment(ordersFragment);
                        return true;

                    case R.id.action_messages:
                        setFragment(alertsFragment);
                        return true;

                    case R.id.action_profile:
                        if(userType.equals("seller")){
                            setFragment(sellerProfileFragment);
                            return true;
                        }else{
                            setFragment(buyerProfileFragment);
                            return true;
                        }

                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
