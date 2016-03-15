package com.aditya.search;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class JsonReader extends ActionBarActivity {

    ArrayList<Locations> locationsList;

    MyLocationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        final double mLatitude = Double.parseDouble(intent.getStringExtra("latitude"));
        final double mLongitude = Double.parseDouble(intent.getStringExtra("longitude"));
        //double mLongitude = -122.4183333;
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location="+mLatitude+","+mLongitude);
        sb.append("&radius=20000");
        sb.append("&types="+"establishment");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyBRiDep644stUwpiu0H59uvPqtdsD8IZcU");
        // String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.775,-122.4183333&radius=20000&types=park|establishment|aquarium&key=AIzaSyBRiDep644stUwpiu0H59uvPqtdsD8IZcU";
        locationsList = new ArrayList<Locations>();

        new JSONAsyncTask().execute(sb.toString());
        ListView listview = (ListView)findViewById(R.id.list);
        adapter = new MyLocationsAdapter(getApplicationContext(), R.layout.row, locationsList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
                try {


                    Toast.makeText(getApplicationContext(), locationsList.get(position).getName(), Toast.LENGTH_LONG).show();

                    Geocoder select_place = new Geocoder(getBaseContext());
                    List<Address> address = select_place.getFromLocationName(locationsList.get(position).getName(), 5);
                    Address location = address.get(0);
                    Toast.makeText(getApplicationContext(),"latitude:" + location.getLatitude() + " Longitude:" + location.getLongitude(),Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(getBaseContext(), GoogleMapsActivity.class);
                    intent1.putExtra("latitude", location.getLatitude() + "");
                    intent1.putExtra("longitude",location.getLongitude() + "");
                    intent1.putExtra("location",locationsList.get(position).getName() + "");
                    startActivity(intent1);
                }
                catch(Exception e)
                {
                    Log.e("Error", "error");
                }
            }
        });
    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(JsonReader.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("results");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Locations loc = new Locations();

                        loc.setName(object.getString("name"));


                       locationsList.add(loc);
                    }
                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            adapter.notifyDataSetChanged();
            if(result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }

}
