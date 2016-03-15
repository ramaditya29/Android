package com.aditya.search;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;
public class MyActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button click;

        click = (Button) findViewById(R.id.find);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText edit = (EditText) findViewById(R.id.places);
                    Geocoder select_place = new Geocoder(getBaseContext());
                    List<Address> address = select_place.getFromLocationName(edit.getText().toString(), 5);
                    Address location = address.get(0);
                    Intent openNewActivity = new Intent(getApplicationContext(), JsonReader.class);
                    openNewActivity.putExtra("latitude",location.getLatitude() + "");
                    openNewActivity.putExtra("longitude",location.getLongitude() + "");
                    startActivity(openNewActivity);
                }
                catch(Exception e)
                {
                    Log.e("Error","Error");
                }
            }
        });
    }
}

