package lk.sliit.easylist.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import lk.sliit.easylist.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView catName;
    public ImageView catImage;
    public CardView cardView;
    public CategoryViewHolder(View itemView){
        super(itemView);
        catName= itemView.findViewById(R.id.textViewCategoryName);
        catImage = itemView.findViewById(R.id.imageViewCategory);
        cardView = itemView.findViewById(R.id.cardViewCategory);
    }
}
