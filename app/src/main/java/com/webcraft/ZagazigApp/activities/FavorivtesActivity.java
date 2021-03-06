package com.webcraft.ZagazigApp.activities ;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.webcraft.ZagazigApp.R;
import com.webcraft.ZagazigApp.RecyclerItemClickListener;
import com.webcraft.ZagazigApp.adapters.SearchResultAdapter;
import com.webcraft.ZagazigApp.dataModels.Place;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavorivtesActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    SearchResultAdapter adapter ;
    RecyclerView.LayoutManager layoutManager ;
    private List<Place> list;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String FAVORITES = "Product_Favorite";
    MediaPlayer mMediaPlayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorivtes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("الاماكن المفضلة");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.ingtone_pop);

        list= new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerListView_favList);
        layoutManager=new LinearLayoutManager(this);
        adapter=new SearchResultAdapter(this,list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if(getCart(this)!=null&&getCart(this).size()!=0){
            list.addAll(getCart(this));
            adapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this, "No items in Favorites.", Toast.LENGTH_LONG).show();
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mMediaPlayer.start();

//                        adapter.getItem(position);
                        Intent i = new Intent(FavorivtesActivity.this, DetailedItemActivity.class);
                        i.putExtra("Item",adapter.getItem(position).getObject().toString());
                        startActivity(i);
                    }

                })
        );
    }
    public ArrayList<Place> getCart(Context context) {
        ArrayList<Place> favorites= new ArrayList<>();
        JSONArray jsonArray ;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(FAVORITES)) {
            String StringFavorites = sharedpreferences.getString(FAVORITES, null);
            try {
                jsonArray = new JSONArray(StringFavorites);
                favorites = new ArrayList<Place>();
                for(int i = 0 ;i<jsonArray.length();i++){
                    favorites.add(new Place(jsonArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else
            return null;
        return favorites;
    }
    public void saveToCart(Context context, List<Place> favorites) {

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        for(int i = 0 ;i<favorites.size();i++){
            jsonArray.put(favorites.get(i).getObject());
        }
        editor.putString(FAVORITES, jsonArray.toString());
        editor.commit();
//        Toast.makeText(FavorivtesActivity.this,"Saved to your Favorites",Toast.LENGTH_SHORT).show();
    }
}
