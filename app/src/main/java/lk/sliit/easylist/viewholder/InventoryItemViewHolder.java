package lk.sliit.easylist.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import lk.sliit.easylist.R;

public class InventoryItemViewHolder extends RecyclerView.ViewHolder {
    public TextView itemName;
    public ImageView itemImage;
    public EditText etQuantity, etPrice;
    public Button btnInc, btnIncPrice;
    public Button btnDec, btnDecPrice;
    public Button btnAddToInventory;
    public InventoryItemViewHolder(View itemView){
        super(itemView);
        itemName= itemView.findViewById(R.id.textViewInventoryItemName);
        itemImage = itemView.findViewById(R.id.imageViewInventoryItem);
        etQuantity = itemView.findViewById(R.id.etQuantity);
        btnInc= itemView.findViewById(R.id.btnIncQuantity);
        btnDec = itemView.findViewById(R.id.btnDecQuantity);
        btnAddToInventory = itemView.findViewById(R.id.btnAddToInventory);
        btnIncPrice = itemView.findViewById(R.id.btnIncUnitPrice);
        btnDecPrice = itemView.findViewById(R.id.btnDecUnitPrice);
        etPrice = itemView.findViewById(R.id.etUnitPrice);
    }
}