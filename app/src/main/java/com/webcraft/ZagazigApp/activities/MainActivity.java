package com.webcraft.ZagazigApp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.webcraft.ZagazigApp.R;
import com.webcraft.ZagazigApp.RecyclerItemClickListener;
import com.webcraft.ZagazigApp.dataModels.Place;
import com.webcraft.ZagazigApp.utilities.APIConfigure;
import com.webcraft.ZagazigApp.utilities.AppController;
import com.webcraft.ZagazigApp.utilities.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Spinner s1;
    Button btn_cat_1, btn_cat_1_1, btn_cat_2, btn_cat_3, btn_cat_4, btn_cat_5;
    ImageButton Image_btn_fav, Image_btn_offer, Image_btn_job, Image_btn_course, Image_btn_search;
    EditText search_input;
    TabLayout tabLayout;
    ProgressDialog pDialog;
    private static String TAG = MainActivity.class.getSimpleName();
    String CONFIG_URL = "http://176.32.230.22/mashaly.net/handler.php?action=config";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs";

    MediaPlayer mMediaPlayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.ingtone_pop);

        s1 = (Spinner) findViewById(R.id.spinner);
        search_input = (EditText) findViewById(R.id.etsearch);
//        tabLayout = (TabLayout) findViewById(R.id.tabs);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.msg_loading));

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        String[] arr = {"كل المناطق", "القومية ", "شارع المحافظة", "مفارق المنصورة", "فلل الجامعة", "حي الزهور", "المنتزة", "شارع البحر", "المحطة", "شارع مديرالامن", "عمر افندي", "حي ثاني", "شارع الغشام", "عمارة الاوقاف"};

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, arr);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        s1.setAdapter(dataAdapter);
        btn_cat_1 = (Button) findViewById(R.id.btn_cat_1);
        btn_cat_1_1=(Button)findViewById(R.id.btn_cat_1_1);
        btn_cat_2 = (Button) findViewById(R.id.btn_cat_2);
        btn_cat_3 = (Button) findViewById(R.id.btn_cat_3);
        btn_cat_4 = (Button) findViewById(R.id.btn_cat_4);
        btn_cat_5 = (Button) findViewById(R.id.btn_cat_5);

        Image_btn_fav = (ImageButton) findViewById(R.id.Btn_fav);
        Image_btn_offer = (ImageButton) findViewById(R.id.Btn_offer);
        Image_btn_job = (ImageButton) findViewById(R.id.Btn_job);
        Image_btn_course = (ImageButton) findViewById(R.id.Btn_course);
        Image_btn_search = (ImageButton) findViewById(R.id.btnsearch);

        Image_btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();

                Intent i = new Intent(MainActivity.this, FavorivtesActivity.class);
                i.putExtra("type", 1);
                startActivity(i);
            }
        });
        Image_btn_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();

//                    Toast.makeText(MainActivity.this,"Offers", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, SectorActivity.class);
                i.putExtra("type", 2);
                startActivity(i);
                // configure
            }
        });
        Image_btn_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();

//                    Toast.makeText(MainActivity.this,"JOBS", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, SectorActivity.class);
                i.putExtra("type", 3);
                startActivity(i);
            }
        });
        Image_btn_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();

//                Toast.makeText(MainActivity.this,"Courses", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, SectorActivity.class);
                i.putExtra("type", 4);
                startActivity(i);
            }
        });

        btn_cat_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();

                Intent i = new Intent(MainActivity.this, CategoryActivity.class);
                i.putExtra("cat_index", 1);
                startActivity(i);
            }
        });
        btn_cat_1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();

                Intent i = new Intent(MainActivity.this, CategoryActivity.class);
                i.putExtra("cat_index", 11);
                startActivity(i);
            }
        });
        btn_cat_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CategoryActivity.class);
                i.putExtra("cat_index", 2);
                startActivity(i);
            }
        });
        btn_cat_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();

                Intent i = new Intent(MainActivity.this, CategoryActivity.class);
                i.putExtra("cat_index", 3);
                startActivity(i);
            }
        });
        btn_cat_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();

                Intent i = new Intent(MainActivity.this, CategoryActivity.class);
                i.putExtra("cat_index", 4);
                startActivity(i);
            }
        });
        btn_cat_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();

                Intent i = new Intent(MainActivity.this, CategoryActivity.class);
                i.putExtra("cat_index", 5);
                startActivity(i);
            }
        });

        search_input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mMediaPlayer.start();

                    // Perform action on key press
                    prepareSearchQuery();

                    return true;
                }
                return false;
            }
        });

        Image_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();
                prepareSearchQuery();
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");

//                    Toast.makeText(getApplicationContext(), "GCM registration token: " + token, Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL

//                    Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
//                    Toast.makeText(getApplicationContext(), "Push notification is received!", Toast.LENGTH_LONG).show();
                }
            }
        };


        if(sharedpreferences.contains("categories")){
            String jsonString = sharedpreferences.getString("categories",null);
            if(jsonString==null){
                makeConfigureRequest();
            }
        }else {
            makeConfigureRequest();
        }
    }

    private void prepareSearchQuery() {
        String url = "";
        if (search_input.getText().length() != 0) {
            if (s1.getSelectedItemPosition() == 0) {
                url = APIConfigure.API_DOMAIN + APIConfigure.API_SEARCH_PATH + "name=" + search_input.getText().toString().replace(" ", "%20");
//                url = APIConfigure.API_DOMAIN + APIConfigure.API_SEARCH_PATH + "name=عربى";
            } else {
                url = APIConfigure.API_DOMAIN + APIConfigure.API_SEARCH_PATH + "area=" + s1.getSelectedItemPosition() + "&name=" + search_input.getText().toString().replace(" ", "%20");
            }

            makeJsonObjectRequest(url);
        }
    }

    private void makeJsonObjectRequest(String URL) {
        showpDialog();
//                        Toast.makeText(MainActivity.this,URL,Toast.LENGTH_LONG).show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                // Parsing json object response
                hidepDialog();
//                Toast.makeText(MainActivity.this,response.toString(), Toast.LENGTH_LONG).show();
                try {
                    if (response.getString("message").toString().equals("success")) {

//                        Toast.makeText(MainActivity.this,response.toString(), Toast.LENGTH_LONG).show();
                        JSONArray data = response.getJSONArray("data");

                        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                        intent.putExtra("data", data.toString());
                        startActivity(intent);

                    } else if (response.getString("message").toString().equals("no data recived")) {
                        Toast.makeText(MainActivity.this, "no places with this name", Toast.LENGTH_LONG).show();

                    } else {
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

                String responseBody = null;
                JSONObject jsonObject = null;
                try {

                    responseBody = new String(error.networkResponse.data, "utf-8");
                    jsonObject = new JSONObject(responseBody);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,R.string.connection_error_msg, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
    private void makeConfigureRequest() {
//        showpDialog();
//                        Toast.makeText(MainActivity.this,URL,Toast.LENGTH_LONG).show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                APIConfigure.API_DOMAIN+APIConfigure.API_CONFIG, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                // Parsing json object response
                hidepDialog();
//                Toast.makeText(MainActivity.this,response.toString(), Toast.LENGTH_LONG).show();
                try {

//                        Toast.makeText(MainActivity.this,response.toString(), Toast.LENGTH_LONG).show();
                    JSONArray data = response.getJSONArray("categories");
                    editor.putString("categories",data.toString());
                    editor.commit();


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

                String responseBody = null;
                JSONObject jsonObject = null;
                try {

                    responseBody = new String(error.networkResponse.data, "utf-8");
                    jsonObject = new JSONObject(responseBody);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,R.string.connection_error_msg, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        mMediaPlayer.start();
//        boolean ico = item.getIcon().equals(R.drawable.abc_btn_rating_star_on_mtrl_alpha );
        switch (id){
            case R.id.action_contactUs:

                final AlertDialog aboutDialog = new AlertDialog.Builder(this).create();
                aboutDialog.setTitle(R.string.app_name);
                aboutDialog.setMessage("(الزقازيق على موبايك)  \n" +
                        "  إدارة شركة فوريو للدعاية و الاعلان \n" +
                        "م/ محمد بهنس  01092822438\n" +
                        "م/ محمد لبيب.  01090250076\n" +
                        "تنفيذ شركة webcraft \n" +
                        "01009606093\n" +
                        "sales@webcraft.ae"); // a message above the buttons
                aboutDialog.setIcon(R.mipmap.ic_launcher); // the icon besides the title you have to change it to the icon/image you have.
                aboutDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { // here you can add a method  to the button clicked. you can create another button just by copying alertDialog.setButton("okay")
                        aboutDialog.dismiss();
                        mMediaPlayer.start();
                    }

                });
                aboutDialog.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
