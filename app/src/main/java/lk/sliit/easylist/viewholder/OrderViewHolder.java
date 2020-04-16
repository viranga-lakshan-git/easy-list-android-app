package lk.sliit.easylist.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lk.sliit.easylist.R;

public class OrderViewHolder extends RecyclerView.ViewHolder{
    public TextView txtName,txtMobile,txtAddress,txtEmail,txtPrice,txtFoundItems;
    Button btnOK,btnCancel;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName=itemView.findViewById(R.id.txt_customer_name_order_cardview);
        txtMobile=itemView.findViewById(R.id.txt_customer_mobile_order_cardview);
        txtAddress=itemView.findViewById(R.id.txt_customer_address_order_cardview);
        txtEmail=itemView.findViewById(R.id.txt_customer_email_order_cardview);
        txtPrice=itemView.findViewById(R.id.txt_price_order_cardview);
        txtFoundItems=itemView.findViewById(R.id.txt_founds_order_cardview);
        btnOK=itemView.findViewById(R.id.btn_action_ok);
        btnCancel=itemView.findViewById(R.id.btn_action_cancel);
    }
}
