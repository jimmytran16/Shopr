package com.example.shoppingapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.shoppingapp.ItemAdapter;
import com.example.shoppingapp.R;
import com.example.shoppingapp.SellerItemDescription;
import com.example.shoppingapp.Upload;
import com.example.shoppingapp.UploadItemActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private GridView gridView ;
    private ImageButton refreshBtn;
    private PageViewModel pageViewModel;
    private List<Upload> buyingList;
    private List<Upload> sellingList;


    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get all Extras from intent
        Bundle extras = getActivity().getIntent().getExtras();
        sellingList = new ArrayList<Upload>();
        //check if intent extras has value
        if(extras!=null) {
            sellingList = (ArrayList<Upload>) extras.getSerializable("sellingList");
            buyingList = (ArrayList<Upload>) extras.getSerializable("wishlist");
        }
        Log.d("PlaceHolderFragment ",""+sellingList);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_activity_user_main_page, container, false);
        gridView = (GridView)root.findViewById(R.id.item_grid_sell_buy);
        refreshBtn= (ImageButton)root.findViewById(R.id.refreshBtn_2);

        pageViewModel.getText().observe(this, new Observer<String>() {
            boolean PAGE_CHECKER = true;
            @Override
            public void onChanged(@Nullable String s) {

                Log.d("PlaceHolderFragment= ",s);
                if(s.equals("1")){
                    PAGE_CHECKER = true;
                    if(buyingList!=null) {
                        gridView.setAdapter(new ItemAdapter(getActivity(), buyingList));
                    }
                }else if(s.equals("2")){
                    PAGE_CHECKER = false;
                    if(sellingList!=null) {
                        gridView.setAdapter(new ItemAdapter(getActivity(), sellingList));
                    }
                }
                //set click listener for clicking on item
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id){

                        Intent goToDetail_intent = new Intent(getActivity(), SellerItemDescription.class);
                        goToDetail_intent.putExtra("position",position);
                        Log.d("PlaceHolderFragment= ",""+position);
                        if(PAGE_CHECKER) {
                            goToDetail_intent.putExtra("buy/sell", (Serializable) buyingList);
                            goToDetail_intent.putExtra("PATH","wishlist");
                        }else if(!PAGE_CHECKER){
                            goToDetail_intent.putExtra("PATH","uploads");
                            goToDetail_intent.putExtra("buy/sell",(Serializable) sellingList);
                        }

                        startActivity(goToDetail_intent);
                    }
                });
                //refreshes the page
                refreshBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                });
            }
        });
        return root;
    }

}
