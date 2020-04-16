package lk.sliit.easylist.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import lk.sliit.easylist.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView itemName;
    public ImageView itemImage;
    public Button btnIncAmount;
    public Button btnDecAmount;
    public Button btnAddToList;
    public EditText etAmount;
    public ItemViewHolder(View itemView){
        super(itemView);
        itemName= itemView.findViewById(R.id.textViewItemName);
        itemImage = itemView.findViewById(R.id.imageViewItem);
        btnIncAmount = itemView.findViewById(R.id.btnIncAmount);
        btnDecAmount = itemView.findViewById(R.id.btnDecAmount);
        etAmount = itemView.findViewById(R.id.etAmount);
        btnAddToList = itemView.findViewById(R.id.btnAddToList);
    }
}