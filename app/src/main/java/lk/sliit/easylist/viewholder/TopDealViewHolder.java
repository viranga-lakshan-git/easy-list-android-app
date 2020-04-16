package lk.sliit.easylist.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import lk.sliit.easylist.R;

public class TopDealViewHolder extends RecyclerView.ViewHolder{
    public TextView textViewName;
    public ImageView imageView;

    public TopDealViewHolder(View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.text_view_name);
        imageView = itemView.findViewById(R.id.image_view_upload);
    }
}
