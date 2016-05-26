package com.webcraft.ZagazigApp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.webcraft.ZagazigApp.R;
import com.webcraft.ZagazigApp.RecyclerItemClickListener;
import com.webcraft.ZagazigApp.adapters.SearchResultAdapter;
import com.webcraft.ZagazigApp.dataModels.Place;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    SearchResultAdapter adapter ;
    RecyclerView.LayoutManager layoutManager ;
    private List<Place> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list= new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerListview_searchResult);
        layoutManager=new LinearLayoutManager(this);
        adapter=new SearchResultAdapter(this,list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

//        list.add(new Place());
//        list.add(new Place());
//        list.add(new Place());
//        list.add(new Place());
//        list.add(new Place());
//        list.add(new Place());
//        list.add(new Place());
//
////        adapter.notifyDataSetChanged();
//        adapter.add(new Place());
//        adapter.add(new Place());
//        adapter.add(new Place());
//        adapter.add(new Place());
//        adapter.add(new Place());
//        adapter.add(new Place());
//        adapter.add(new Place());
//        adapter.add(new Place());

        if(getIntent()!=null&&getIntent().getExtras()!=null){
            try {
                JSONArray jsonArray = new JSONArray(getIntent().getExtras().getString("data"));
                for(int i =0 ;i<jsonArray.length();i++){
                    adapter.add(new Place(jsonArray.getJSONObject(i)));
//                            Toast.makeText(CategoryActivity.this,data.getJSONObject(i).getString("icoImage"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        adapter.getItem(position);
                        Intent i = new Intent(SearchResultActivity.this,DetailedItemActivity.class);
                        i.putExtra("Item",adapter.getItem(position).getObject().toString());
                        startActivity(i);
                    }

                })
        );
    }

}
