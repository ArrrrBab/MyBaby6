package com.example.mybaby6.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mybaby6.MyInterface;
import com.example.mybaby6.R;
import com.example.mybaby6.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HomeFragment extends Fragment implements MyInterface {
    public ArrayAdapter adapter;
    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView list = binding.theList;
        final EditText thefilter = binding.searchFilter;
        final ImageView imageView = binding.imageView;

        setAdapter(list);
        //search a product
        thefilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter != null)
                (HomeFragment.this).adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //choose a product
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String s = (String) adapter.getItem(position);
                if (!basket.contains(s + " 1")) basket.add(s + " 1");
            }
        });
        return root;
    }
    public void setAdapter(ListView list){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        for (Object shop: ((HashMap<String, HashMap>) dataSnapshot.getValue()).keySet().toArray()){
                            for (Object type:((HashMap<String, HashMap>) dataSnapshot.child((String) shop).getValue()).keySet().toArray()) {
                                for (Object brand : ((HashMap<String, HashMap>) dataSnapshot.child((String) shop).child((String) type).getValue()).keySet().toArray()) {
                                    allProductsPrice.add(new ArrayList<String>(Arrays.asList((String) shop,
                                            (String) type + " " + (String) brand,
                                            String.valueOf(dataSnapshot.child((String) shop).child((String) type).child((String) brand).child("Цена").getValue()))));
                                    allProductsLink.add(new ArrayList<String>(Arrays.asList((String) shop,
                                            String.valueOf(dataSnapshot.child((String) shop).child((String) type).child((String) brand).child("Ссылка").getValue()))));
                                    if (!allProducts.contains((String) type + " " + (String) brand)) {
                                        allProducts.add((String) type + " " + (String) brand);
                                    }
                                }
                                if (!categories.contains((String) type)){
                                    categories.add((String) type);
                                }
                            }
                            if (!shops.contains(shop)) shops.add((String) shop);
                        }
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Sorry, service is unavailable", Toast.LENGTH_SHORT).show();
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
        adapter = new ArrayAdapter(getActivity(), R.layout.list_item_layout, allProducts);
        list.setAdapter(adapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}