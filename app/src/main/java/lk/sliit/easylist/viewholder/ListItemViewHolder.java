package lk.sliit.easylist.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import lk.sliit.easylist.R;

public class ListItemViewHolder extends RecyclerView.ViewHolder{
    public TextView txtItemName;
    public TextView txtItemQty;

    public ListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        txtItemName=itemView.findViewById(R.id.tv_itemName);
        txtItemQty=itemView.findViewById(R.id.tv_itemQty);
    }
}
