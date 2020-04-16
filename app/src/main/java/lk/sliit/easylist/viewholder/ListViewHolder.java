package lk.sliit.easylist.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lk.sliit.easylist.R;

public class ListViewHolder extends RecyclerView.ViewHolder{
    public TextView textViewListName,textViewListType;
    public Button buttonViewEdit;
    public Button buttonDeleteList;

    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewListType=itemView.findViewById(R.id.tvListType);
        textViewListName=itemView.findViewById(R.id.tvListName);
        buttonViewEdit=itemView.findViewById(R.id.btnViewEditList);
        buttonDeleteList=itemView.findViewById(R.id.btnDeleteList);
    }
}
