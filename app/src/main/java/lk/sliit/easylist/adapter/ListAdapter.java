package lk.sliit.easylist.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.fragment.CandidateSellersFragment;
import lk.sliit.easylist.fragment.ViewListFragment;
import lk.sliit.easylist.module.List;
import lk.sliit.easylist.viewholder.ListViewHolder;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder>{
    public Context context;
    public ArrayList<List> lists;

    FirebaseAuth firebaseAuth;
    String userType;
    int j;

    public ListAdapter(Context c , ArrayList<List> l, String userType){
        context=c;
        lists=l;
        this.userType=userType;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
        firebaseAuth= FirebaseAuth.getInstance();
        holder.textViewListType.setText(lists.get(position).getListType());
        holder.textViewListName.setText(lists.get(position).getListName());


        holder.buttonDeleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("Confirm");
                builder.setMessage("Delete list: "+lists.get(position).getListName()+"?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference databaseReferenceLists= FirebaseDatabase.getInstance().getReference("Buyer").child(firebaseAuth.getCurrentUser().getUid()).child("Lists").child(lists.get(position).getListType());
                                Query query= databaseReferenceLists.orderByChild("listName").equalTo(lists.get(position).getListName());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().removeValue();
                                        Toast.makeText(context, lists.get(position).getListName()+" list deleted.", Toast.LENGTH_LONG).show();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.buttonViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("LIST_NAME",lists.get(position).getListName());
                bundle.putString("LIST_TYPE",lists.get(position).getListType());
                bundle.putString("USER_TYPE",userType);
                ViewListFragment viewListFragment=new ViewListFragment();
                viewListFragment.setArguments(bundle);
                setFragment(viewListFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
