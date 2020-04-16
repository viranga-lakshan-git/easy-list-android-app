package lk.sliit.easylist.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import lk.sliit.easylist.R;

public class InventoryListViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgItem;
    public TextView txtItemName, txtStock, txtPrice;
    public Button btnDecStock, btnIncStock, btnDecPrice, btnIncPrice, btnDeleteItem, btnUpdateItem;
    public InventoryListViewHolder(View itemView){
        super(itemView);
        imgItem=itemView.findViewById(R.id.imageViewItem);
        txtItemName=itemView.findViewById(R.id.txtViewItemName);
        txtStock=itemView.findViewById(R.id.etStock);
        txtPrice=itemView.findViewById(R.id.etPrice);
        btnDecStock=itemView.findViewById(R.id.btnDecStock);
        btnIncStock=itemView.findViewById(R.id.btnIncStock);
        btnDecPrice=itemView.findViewById(R.id.btnDecPrice);
        btnIncPrice=itemView.findViewById(R.id.btnIncPrice);
        btnUpdateItem=itemView.findViewById(R.id.btnUpdateItem);
        btnDeleteItem=itemView.findViewById(R.id.btnDeleteItem);
    }
}
