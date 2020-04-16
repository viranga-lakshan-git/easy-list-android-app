package lk.sliit.easylist.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import lk.sliit.easylist.R;
import lk.sliit.easylist.adapter.CandidateOrderAdapter;
import lk.sliit.easylist.module.Order;

/**
 * A simple {@link Fragment} subclass.
 */
public class CandidateSellersFragment extends Fragment {
    public CandidateSellersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_candidate_sellers, container, false);

        TextView txtSellerName = view.findViewById(R.id.tv_seller_name_orderview);
        TextView txtShopName = view.findViewById(R.id.tv_shop_name_orderview);
        TextView txtEmail = view.findViewById(R.id.tv_seller_email_orderview);
        TextView txtContact = view.findViewById(R.id.tv_seller_contact_orderview);
        TextView txtShopAddress = view.findViewById(R.id.tv_shop_address_orderview);
        TextView txtPercentage = view.findViewById(R.id.tv_items_percentage_orderview);
        TextView txtPrice = view.findViewById(R.id.tv_seller_price_orderview);
        TextView txtFoundItems = view.findViewById(R.id.tv_found_items_orderview);
        TextView txtNotFoundItems = view.findViewById(R.id.tv_not_found_items_orderview);
        Button btnViewSellerLocation = view.findViewById(R.id.btn_view_seller_location);


        TextView txtListType = view.findViewById(R.id.txt_selected_list_type_orderview);
        TextView txtListName = view.findViewById(R.id.txt_list_name_orderview);

        Bundle bundle=getArguments();
        String listType = bundle.getString("LIST_TYPE");
        String listName = bundle.getString("LIST_NAME");

        txtListType.setText(listType);
        txtListName.setText(listName);

        RecyclerView rvCandidateOrders = view.findViewById(R.id.rv_candidate_orders);
        rvCandidateOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Order> orderArrayList = bundle.getParcelableArrayList("CANDIDATE_ORDERS");
        Collections.sort(orderArrayList);

        CandidateOrderAdapter candidateOrderAdapter = new CandidateOrderAdapter(getContext(), orderArrayList, txtSellerName, txtShopName, txtEmail, txtContact, txtShopAddress, txtPercentage, txtPrice, txtFoundItems, txtNotFoundItems, btnViewSellerLocation);
        rvCandidateOrders.setAdapter(candidateOrderAdapter);

        return view;
    }

}
