package lk.sliit.easylist.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import lk.sliit.easylist.R;

public class CandidateOrderViewHolder extends RecyclerView.ViewHolder{
    public TextView txtShopName, txtPrice, txtPercentage;
    public Button btnReqOrder;
    public CardView cardView;
    public CandidateOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtShopName=itemView.findViewById(R.id.tv_shop_name);
        txtPrice=itemView.findViewById(R.id.tv_price);
        txtPercentage=itemView.findViewById(R.id.tv_percentage);
        btnReqOrder = itemView.findViewById(R.id.btn_request_order);
        cardView = itemView.findViewById(R.id.cardview_candidate_order);
    }
}
