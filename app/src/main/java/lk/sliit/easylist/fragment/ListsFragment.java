package lk.sliit.easylist.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lk.sliit.easylist.R;
import lk.sliit.easylist.adapter.ListAdapter;
import lk.sliit.easylist.module.List;
import lk.sliit.easylist.module.ListItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListsFragment extends Fragment {
    private DatabaseReference databaseReferenceLists;
    private EditText editTextListName;
    private Spinner spinnerListType;
    private String[] types = new String[] {"Emergency", "Wish List", "Special", "Daily", "Weekend", "Weekly", "Monthly", "Drafts"};
    private RecyclerView recyclerViewLists;
    private ArrayList<List> listArrayList;
    private List list;
    private int i;
    private String userType;

    public ListsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        userType= getActivity().getIntent().getStringExtra("USER_TYPE");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceLists = FirebaseDatabase.getInstance().getReference("Buyer").child(firebaseAuth.getCurrentUser().getUid()).child("Lists");
        editTextListName=view.findViewById(R.id.editTextNewList);
        spinnerListType=view.findViewById(R.id.spinnerListType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListType.setAdapter(adapter);
        Button buttonCreateList = view.findViewById(R.id.btnCreateList);

        recyclerViewLists=view.findViewById(R.id.recyclerViewLists);
        recyclerViewLists.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        ArrayList<ListItem> listItemsArrayList = new ArrayList<>();

        buttonCreateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName=editTextListName.getText().toString();
                if(!listName.isEmpty()) {
                    list = new List(listName, spinnerListType.getSelectedItem().toString());

                    Query query = databaseReferenceLists.child(list.getListType()).orderByKey().equalTo(listName);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() == 0) {
                                Toast.makeText(getContext(), "List created", Toast.LENGTH_LONG).show();
                                databaseReferenceLists.child(list.getListType()).child(list.getListName()).setValue(list);
                            } else {
                                Toast.makeText(getContext(), "A List in this name already exists.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Enter a List Name first.", Toast.LENGTH_LONG).show();
                }
            }
        });

        databaseReferenceLists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                i=0;
                listArrayList=new ArrayList<>();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    for(DataSnapshot d2 : d.getChildren()) {
                        i++;
                        List list = d2.getValue(List.class);
                        listArrayList.add(list);
                        if (i == dataSnapshot.getChildrenCount()) {
                            ListAdapter listAdapter = new ListAdapter(getContext(), listArrayList, userType);
                            recyclerViewLists.setAdapter(listAdapter);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Something went wrong...",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

}
