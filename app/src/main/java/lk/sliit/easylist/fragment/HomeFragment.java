package lk.sliit.easylist.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;

import lk.sliit.easylist.R;
import lk.sliit.easylist.adapter.CategoryAdapter;
import lk.sliit.easylist.adapter.InventoryItemAdapter;
import lk.sliit.easylist.adapter.ItemAdapter;
import lk.sliit.easylist.adapter.TopDealAdapter;
import lk.sliit.easylist.function.GoogleTranslator;
import lk.sliit.easylist.function.Resultset;
import lk.sliit.easylist.module.Category;
import lk.sliit.easylist.module.Item;
import lk.sliit.easylist.module.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private StorageReference storageReferenceCategories,storageReferenceItems;
    private FirebaseAuth firebaseAuth;
    private String translatedText;

    private Spinner spinnerLists;
    private RecyclerView recyclerViewCategory,recyclerViewItem,recyclerViewTopDeals;
    private ArrayList<Category> categoryArrayList;
    private ArrayList<Item> itemArrayList;
    private ArrayList<String> categoryNamesList;
    private String userType;
    private List selectedList;
    private int i,j,k;
    private EditText editTextSearch;
    private java.util.List<Resultset> gotResults = new ArrayList<>();


    private List getSelectedList(){
        return this.selectedList;
    }

    private void setSelectedList(List list){this.selectedList=list;}

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);


        Button btnGetVoiceInput = view.findViewById(R.id.imgViewMicBtn);
        Button btnSearch = view.findViewById(R.id.buttonSearch);

        userType= getActivity().getIntent().getStringExtra("USER_TYPE");
        gotResults = (ArrayList<Resultset>)getActivity().getIntent().getSerializableExtra("selectedList");

        spinnerLists=view.findViewById(R.id.spinnerSelectedList);
        if(userType.equals("seller")){
            spinnerLists.setVisibility(View.GONE);
            view.findViewById(R.id.textViewSelectList).setVisibility(View.GONE);
        }

        // firebase buyer's created lists

        firebaseAuth= FirebaseAuth.getInstance();
        DatabaseReference databaseReferenceLists = FirebaseDatabase.getInstance().getReference("Buyer").child(firebaseAuth.getCurrentUser().getUid()).child("Lists");
        databaseReferenceLists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                ArrayList<List> listArrayList=new ArrayList<>();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    for(DataSnapshot d2 : d.getChildren()) {
                        List list = d2.getValue(List.class);
                        listArrayList.add(list);
                    }
                }
                if(getContext()!=null) {
                    ArrayAdapter<List> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listArrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLists.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Loading lists went wrong...",Toast.LENGTH_LONG).show();
            }
        });


        spinnerLists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List list=(List)parent.getItemAtPosition(position);
                setSelectedList(list);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        recyclerViewCategory=view.findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        //firebase categories

        DatabaseReference databaseReferenceCategories = FirebaseDatabase.getInstance().getReference().child("Category");
        databaseReferenceCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                i=0;
                categoryArrayList=new ArrayList<>();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    i++;
                    categoryNamesList = new ArrayList<>();
                    final Category category=d.getValue(Category.class);
                    assert category != null;
                    categoryNamesList.add(category.getCategoryName());
                    storageReferenceCategories = FirebaseStorage.getInstance().getReference();
                    storageReferenceCategories.child(category.getImgURL()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            category.setImgURL(uri.toString());
                            categoryArrayList.add(category);
                            if(i==dataSnapshot.getChildrenCount()){
                                setCategoryRecyclerView(categoryArrayList);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Something went wrong...",Toast.LENGTH_LONG).show();
            }
        });

        //datamining

        recyclerViewTopDeals=view.findViewById(R.id.recycleviewTopSelling);
        recyclerViewTopDeals.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Item");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final ArrayList<Item> itmList = new ArrayList<>();
                for (int s = 0; s <4 ; s++) {
                    String name = gotResults.get(s).getRs();
                    final String n = name.substring(1,(name.length()-1));
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        for (DataSnapshot d2 : ds.getChildren()) {
                            final Item i = d2.getValue(Item.class);
                            if (i.getItemName().equals("" + n)) {
                                storageReferenceItems = FirebaseStorage.getInstance().getReference();
                                assert i != null;
                                storageReferenceItems.child(i.getImgURL()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        i.setImgURL(uri.toString());
                                        itmList.add(i);
                                        setTopDetailsRecyclerView(itmList);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //firebase items

        recyclerViewItem=view.findViewById(R.id.recyclerViewItems);
        recyclerViewItem.setLayoutManager(new GridLayoutManager(getContext(), 3));
        DatabaseReference databaseReferenceItems = FirebaseDatabase.getInstance().getReference().child("Item");
        databaseReferenceItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                j=0;
                itemArrayList= new ArrayList<>();
                for(DataSnapshot d : dataSnapshot.getChildren()) {
                    j++;
                    for (DataSnapshot d2 : d.getChildren()) {
                        final Item item = d2.getValue(Item.class);
                        storageReferenceItems = FirebaseStorage.getInstance().getReference();
                        assert item != null;
                        storageReferenceItems.child(item.getImgURL()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                item.setImgURL(uri.toString());
                                itemArrayList.add(item);
                                if(j==dataSnapshot.getChildrenCount()){
                                    setItemRecyclerView(itemArrayList);
                                }
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Something went wrong with database connectivity...",Toast.LENGTH_LONG).show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextSearch.getText().toString();
                if(!text.isEmpty() || text.equals(null)) {
                    GoogleTranslator googleTranslator = new GoogleTranslator();
                    googleTranslator.setText(text);

                    Thread thread = new Thread(googleTranslator);
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    translatedText = googleTranslator.getTranslatedText();
                    final ArrayList<Category> searchedCategories = new ArrayList<>();
                    ArrayList<Item> searchedItems = new ArrayList<>();

                    for (Category c : categoryArrayList) {
                        if (c.getCategoryName().toLowerCase().contains(translatedText.toLowerCase())) {
                            searchedCategories.add(c);
                        }
                    }
                    for (Item item : itemArrayList) {
                        if (item.getItemName().toLowerCase().contains(translatedText.toLowerCase())) {
                            searchedItems.add(item);
                        }
                    }

                    if (searchedCategories.size() == 0 && searchedItems.size() == 0) {
                        Toast.makeText(getContext(), "Searched item not found.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Items found on searched results...", Toast.LENGTH_SHORT).show();
                        setCategoryRecyclerView(searchedCategories);
                        setItemRecyclerView(searchedItems);
                    }
                    StringBuilder selectedItems = new StringBuilder();
                    selectedItems.append(":").append(translatedText);
                }else {
                    setCategoryRecyclerView(categoryArrayList);
                    setItemRecyclerView(itemArrayList);
                }
            }
        });

        btnGetVoiceInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getAvailableLocales());

                if(intent.resolveActivity(getActivity().getPackageManager())!=null) {
                    startActivityForResult(intent, 10);
                }else{
                    Toast.makeText(getContext(),"Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                editTextSearch.setText(result.get(0));
            }
        }
    }

    private void setCategoryRecyclerView(ArrayList<Category> categories){
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories, recyclerViewItem, userType, getSelectedList());
        recyclerViewCategory.setAdapter(categoryAdapter);
    }

    private void setTopDetailsRecyclerView(ArrayList<Item> topDeals){
        TopDealAdapter topdealAdapter = new  TopDealAdapter(getContext(), topDeals);
        recyclerViewTopDeals.setAdapter(topdealAdapter);

    }

    private void setItemRecyclerView(ArrayList<Item> items){
        if(userType.equals("buyer")) {
            ItemAdapter itemAdapter = new ItemAdapter(getContext(), items, getSelectedList());
            recyclerViewItem.setAdapter(itemAdapter);
        }else{
            InventoryItemAdapter inventoryItemAdapter = new InventoryItemAdapter(getContext(), items, firebaseAuth.getCurrentUser().getUid());
            recyclerViewItem.setAdapter(inventoryItemAdapter);
        }
    }


}
