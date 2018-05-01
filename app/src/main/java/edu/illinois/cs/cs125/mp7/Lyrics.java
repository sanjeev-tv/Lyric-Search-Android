package edu.illinois.cs.cs125.mp7;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Lyrics extends AppCompatActivity {

    private static final String TAG = "Lyrics:Main";

    private String artist;
    private String song;
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_lyrics);
        final Button search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EditText artistInput = (EditText) findViewById(R.id.artist);
                EditText songInput = (EditText) findViewById(R.id.song);
                Log.d(TAG, "Start API button clicked");
                if (artistInput == null) {
                    Log.d(TAG, "Failed to get Edit text object");
                }
                Editable textinp = artistInput.getText();
                if (textinp == null) {
                    Log.d(TAG, "Failed to get Edit text object");
                }
                artist = textinp.toString();
                System.out.println(artist);
                song = songInput.getText().toString();
                System.out.println(song);
                startAPICall();
            }
        });
    }

    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.lyrics.ovh/v1/" + artist + "/" + song,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            final TextView myTextBox = findViewById(R.id.lyrics);
                            try {
                                myTextBox.setText(response.getString("lyrics"));
                                Log.d(TAG, response.toString());
                            } catch (Exception e) {
                                Log.w(TAG, e.toString());
                                Context context = getApplicationContext();
                                CharSequence text = "Song not found!";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                    Context context = getApplicationContext();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
