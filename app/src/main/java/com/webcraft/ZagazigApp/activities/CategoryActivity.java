package com.webcraft.ZagazigApp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.webcraft.ZagazigApp.R;
import com.webcraft.ZagazigApp.RecyclerItemClickListener;
import com.webcraft.ZagazigApp.adapters.SearchResultAdapter;
import com.webcraft.ZagazigApp.dataModels.Place;
import com.webcraft.ZagazigApp.dataModels.SubCategory;
import com.webcraft.ZagazigApp.utilities.APIConfigure;
import com.webcraft.ZagazigApp.utilities.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    TextView swipeText ;
    Spinner sp_areas , sp_tags;
    ProgressDialog pDialog ;
    private static String TAG = CategoryActivity.class.getSimpleName();
    RecyclerView recyclerView ;
    SearchResultAdapter adapter ;
    RecyclerView.LayoutManager layoutManager ;
    ArrayList<Place> list = new ArrayList<>();
    ArrayList<Place>holder = new ArrayList<>();

    SwipeRefreshLayout mSwipeRefreshLayout;

    String URL ,area,sub_cat;
    int index ;
    boolean isFirstTime=true ;
    RelativeLayout category_bk ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String OFFLINECAT1="cat1";
    public static final String OFFLINECAT2="cat2";
    public static final String OFFLINECAT3="cat3";
    public static final String OFFLINECAT4="cat4";
    public static final String OFFLINECAT5="cat5";

    ArrayList<String> subcategory = new ArrayList<>();
    ArrayList<SubCategory> subs = new ArrayList<>();

    ArrayAdapter<String> tagsAdapter ;
    String URL_NEXT_PAGE ="";
    String URL_PREV_PAGE ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        swipeText = (TextView) findViewById(R.id.swipeText);
        swipeText.setVisibility(View.INVISIBLE);

        category_bk = (RelativeLayout) findViewById(R.id.category_bk);
        index = getIntent().getExtras().getInt("cat_index");
        tagsAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, subcategory);
        tagsAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        tagsAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        switch (index){
            case 1:
                getSupportActionBar().setTitle("خروجات");
                getSupportActionBar().setIcon(R.mipmap.cat_khorogat);
                getSubCatsDropDownMenu(1);
                break;
            case 11:
                getSupportActionBar().setTitle("مطاعم");
                getSupportActionBar().setIcon(R.mipmap.cat_rest);
                getSubCatsDropDownMenu(1);
                break;
            case 2:
                getSupportActionBar().setTitle("أناقة");
                getSupportActionBar().setIcon(R.mipmap.cat_anaka);
                getSubCatsDropDownMenu(2);
                break;
            case 3:
                getSupportActionBar().setTitle("صحة وجمال");
                getSupportActionBar().setIcon(R.mipmap.cat_sahawegamal);
                getSubCatsDropDownMenu(3);
                break;
            case 4:
                getSupportActionBar().setTitle("افراح ومناسبات");
                getSupportActionBar().setIcon(R.mipmap.cat_afrah);
                getSubCatsDropDownMenu(4);
                break;
            case 5:
                getSupportActionBar().setTitle("خدمات");
                getSupportActionBar().setIcon(R.mipmap.cat_khadmat);
                getSubCatsDropDownMenu(5);
                break;
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.msg_loading));

        String [] arr ={"كل المناطق","القومية ","شارع المحافظة","مفارق المنصورة","فلل الجامعة","حي الزهور","المنتزة","شارع البحر","المحطة","شارع مديرالامن","عمر افندي","حي ثاني","شارع الغشام" ,"عمارة الاوقاف"};
//        String [] subcategory = {"الكل","مطاعم","كافيهات","سينمات","هدوم ولادى","هدوم بناتى","هدوم اطفال","موبيلات ولابات","جيم شبابي","جيم بناتى","مراكز تجميل","قاعات افراح","ستوديو تصوير","فوتوجرافيك","مستشفيات","عيادات","خدمات عربيات"};

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, arr);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);



        sp_areas = (Spinner) findViewById(R.id.sp_aresa);
        sp_areas.setAdapter(dataAdapter);

        sp_tags= (Spinner) findViewById(R.id.sp_subCats);
        sp_tags.setAdapter(tagsAdapter);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerListview_category);
        layoutManager=new LinearLayoutManager(this);
        adapter=new SearchResultAdapter(this,list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        area="";
        sub_cat="";

//        Toast.makeText(CategoryActivity.this,adapter.getItemCount()+"", Toast.LENGTH_LONG).show();

        sp_areas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    isFirstTime=false;
                    area=position+"";

                }else {
                    area="";
                }
                if(!isFirstTime){
                    makeJsonObjectRequest(URL,true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_tags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    isFirstTime=false;
//                    sub_cat = position+"";
                    sub_cat = subs.get(position).getId();
//                    Toast.makeText(CategoryActivity.this,sub_cat + " id of that item ", Toast.LENGTH_LONG).show();

                }else {
                    sub_cat="";
                }

                if(!isFirstTime){
                    sub_cat = subs.get(position).getId();
                    makeJsonObjectRequest(URL+sub_cat,true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent i = new Intent(CategoryActivity.this, DetailedItemActivity.class);
                        i.putExtra("Item",adapter.getItem(position).getObject().toString());
                        startActivity(i);
                    }

                })
        );

        if(index==11){
            index=1;
            URL = APIConfigure.API_DOMAIN+APIConfigure.API_SEARCH_PATH+"category="+index+"&area="+area+ "&sub_category="+sub_cat;
        }else {
            URL = APIConfigure.API_DOMAIN+APIConfigure.API_SEARCH_PATH+"category="+index+"&area="+area+ "&sub_category="+sub_cat;
        }
        makeJsonObjectRequest(URL,true);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(URL_NEXT_PAGE.equals("null")) {
                    Toast.makeText(CategoryActivity.this, "There is no more items to show.", Toast.LENGTH_LONG).show();
                    if(mSwipeRefreshLayout.isRefreshing()){
                        mSwipeRefreshLayout.setRefreshing(false);
                        return ;
                    }
                }else if(URL_NEXT_PAGE.length()>4){
                    makeJsonObjectRequest(URL+URL_NEXT_PAGE,false);
                }else if(URL_NEXT_PAGE.equals("")){
                    makeJsonObjectRequest(URL,true);
                }else {
                    Toast.makeText(CategoryActivity.this, "3rd", Toast.LENGTH_LONG).show();
                    if(mSwipeRefreshLayout.isRefreshing()){
                        mSwipeRefreshLayout.setRefreshing(false);
                        return ;
                    }
                }

            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

    }

    private void makeJsonObjectRequest(String url,final boolean clear) {
//        Toast.makeText(CategoryActivity.this,url, Toast.LENGTH_LONG).show();

        if(!mSwipeRefreshLayout.isRefreshing()){
            showpDialog();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                // Parsing json object response
                hidepDialog();
                adapter.clear();
                try {
                    if(response.getString("message").toString().equals("no data recived")){
                        Toast.makeText(CategoryActivity.this,"no places with this name", Toast.LENGTH_LONG).show();

                    }else if(response.getString("message").toString().equals("success")){
                        swipeText.setVisibility(View.VISIBLE);
                        if(mSwipeRefreshLayout.isRefreshing()){
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        JSONArray data = response.getJSONArray("data");
                        if(clear){
                            list.clear();
                            holder.clear();
                            switch (index){
                                case 1:
                                    editor.putString(OFFLINECAT1,data.toString());
                                    break;
                                case 2:
                                    editor.putString(OFFLINECAT2,data.toString());
                                    break;
                                case 3:
                                    editor.putString(OFFLINECAT3,data.toString());
                                    break;
                                case 4:
                                    editor.putString(OFFLINECAT4,data.toString());
                                    break;
                                case 5:
                                    editor.putString(OFFLINECAT5,data.toString());
                                    break;
                                default:
                                    break;
                            }
                            editor.commit();
                        }else {
                            switch (index){
                                case 1:
                                    //make method to append
//                                    getSharedData(OFFLINECAT1);
//                                    concatArray(getSharedData(OFFLINECAT1),data);
                                    editor.putString(OFFLINECAT1,concatArray(getSharedData(OFFLINECAT1),data).toString());
                                    break;
                                case 2:
                                    editor.putString(OFFLINECAT2,concatArray(getSharedData(OFFLINECAT2),data).toString());
                                    break;
                                case 3:
                                    editor.putString(OFFLINECAT3,concatArray(getSharedData(OFFLINECAT3),data).toString());
                                    break;
                                case 4:
                                    editor.putString(OFFLINECAT4,concatArray(getSharedData(OFFLINECAT4),data).toString());
                                    break;
                                case 5:
                                    editor.putString(OFFLINECAT5,concatArray(getSharedData(OFFLINECAT5),data).toString());
                                    break;
                                default:
                                    break;
                            }
                            editor.commit();
                        }

//                        Toast.makeText(CategoryActivity.this, data.toString(), Toast.LENGTH_LONG).show();


//                        Toast.makeText(CategoryActivity.this,holder.size()+" item before", Toast.LENGTH_LONG).show();
                        for(int i =0 ;i<data.length();i++){
//                            adapter.add(new Place(data.getJSONObject(i)));
//                            list.add(0,new Place(data.getJSONObject(i)));
                            holder.add(0,new Place(data.getJSONObject(i)));
                        }
                        list.addAll(holder);
//                        Toast.makeText(CategoryActivity.this,holder.size()+" item after", Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                        URL_NEXT_PAGE = response.getString("next_page_url");
                        URL_PREV_PAGE = response.getString("prev_page_url");

                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                                "Something went wrong..!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                hidepDialog();
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                String responseBody = null;
                JSONObject jsonObject=null ;
                try {

                    responseBody = new String( error.networkResponse.data, "utf-8" );
                    jsonObject = new JSONObject( responseBody );

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CategoryActivity.this,R.string.connection_error_msg,Toast.LENGTH_LONG).show();
                    switch (index){
                        case 1:
                            if(sharedpreferences.contains(OFFLINECAT1)){
                                populateData(sharedpreferences.getString(OFFLINECAT1,null));
                            }
                            break;
                        case 2:
                            if(sharedpreferences.contains(OFFLINECAT2)){
                                populateData(sharedpreferences.getString(OFFLINECAT2,null));
                            }
                            break;
                        case 3:
                            if(sharedpreferences.contains(OFFLINECAT3)){
                                populateData(sharedpreferences.getString(OFFLINECAT3,null));
                            }
                            break;
                        case 4:
                            if(sharedpreferences.contains(OFFLINECAT4)){
                                populateData(sharedpreferences.getString(OFFLINECAT4,null));
                            } //
                            break;
                        case 5:
                            if(sharedpreferences.contains(OFFLINECAT5)){
                                populateData(sharedpreferences.getString(OFFLINECAT5,null));
                            }
                            break;
                        default:
                            break;

                    }
                }
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
    private void getSubCatsDropDownMenu(int index){
        if(sharedpreferences.contains("categories")){
            String jsonString = sharedpreferences.getString("categories",null);
            if(jsonString!=null){
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    JSONObject cat = jsonArray.getJSONObject(index-1);
                    JSONArray subCategories = cat.getJSONArray("subCategories");
                    for(int i = 0 ;i<subCategories.length();i++){
                        subcategory.add(subCategories.getJSONObject(i).getString("description"));
                        subs.add(new SubCategory(subCategories.getJSONObject(i)));
                        tagsAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private void populateData(String dataString) {

        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject(dataString);
//            JSONArray data = jsonObject1.getJSONArray("data");
            JSONArray data = new JSONArray(dataString);
            adapter.clear();
            for(int i =0 ;i<data.length();i++) {
                adapter.add(new Place(data.getJSONObject(i)));
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
    private JSONArray getSharedData(String dataStringTag){
        String dataString ;
        JSONArray data = null;
        if(sharedpreferences.contains(dataStringTag)){
            dataString =  sharedpreferences.getString(OFFLINECAT1,null);
            try {
                data = new JSONArray(dataString);
//                data.put(0,"SSS");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
    private JSONArray concatArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
