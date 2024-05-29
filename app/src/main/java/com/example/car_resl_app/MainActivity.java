package com.example.car_resl_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Spinner abtest, vechileType, gearbox, model, fuelType, brand, notRepairedDamage;
    private EditText Yor, powerPs, kilometer, monthOfRegistration;
    private Button predict;
    private TextView result;
    private String url = "https://car-app-api.onrender.com/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Spinners
        abtest = findViewById(R.id.abtest);
        vechileType = findViewById(R.id.vechileType);
        gearbox = findViewById(R.id.gearbox);
        model = findViewById(R.id.model);
        fuelType = findViewById(R.id.fuelType);
        brand = findViewById(R.id.brand);
        notRepairedDamage = findViewById(R.id.notRepairedDamage);

        // Initialize EditTexts
        Yor = findViewById(R.id.Yor);
        powerPs = findViewById(R.id.PowerPs);
        kilometer = findViewById(R.id.kilometer);
        monthOfRegistration = findViewById(R.id.monthOfRegistration);

        // Initialize Button and TextView
        predict = findViewById(R.id.predict);
        result = findViewById(R.id.result);

        // Set up Spinners with data
        setUpSpinners();

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String predictedPrice = jsonObject.getString("The expected resale value in INR is");
                                    String displayText = "Predicted price is : ₹" + predictedPrice;
                                    result.setText(displayText);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "Invalid JSON response", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("abtest", String.valueOf(abtest.getSelectedItemPosition()));
                        params.put("vechileType", String.valueOf(vechileType.getSelectedItemPosition()));
                        params.put("Yor", Yor.getText().toString());
                        params.put("gearbox", String.valueOf(gearbox.getSelectedItemPosition()));
                        params.put("PowerPs", powerPs.getText().toString());
                        params.put("model", String.valueOf(model.getSelectedItemPosition()));
                        params.put("kilometer", kilometer.getText().toString());
                        params.put("monthOfRegistration", monthOfRegistration.getText().toString());
                        params.put("fuelType", String.valueOf(fuelType.getSelectedItemPosition()));
                        params.put("brand", String.valueOf(brand.getSelectedItemPosition()));
                        params.put("notRepairedDamage", String.valueOf(notRepairedDamage.getSelectedItemPosition()));
                        return params;
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null && response.data != null) {
                            try {
                                responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            } catch (Exception e) {
                                responseString = new String(response.data);
                            }
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }

    private void setUpSpinners() {
        ArrayAdapter<CharSequence> abtestAdapter = ArrayAdapter.createFromResource(this,
                R.array.abtest_options, android.R.layout.simple_spinner_item);
        abtestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        abtest.setAdapter(abtestAdapter);

        ArrayAdapter<CharSequence> vechileTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.vechile_type_options, android.R.layout.simple_spinner_item);
        vechileTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vechileType.setAdapter(vechileTypeAdapter);

        ArrayAdapter<CharSequence> gearboxAdapter = ArrayAdapter.createFromResource(this,
                R.array.gearbox_options, android.R.layout.simple_spinner_item);
        gearboxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gearbox.setAdapter(gearboxAdapter);

        ArrayAdapter<CharSequence> modelAdapter = ArrayAdapter.createFromResource(this,
                R.array.model_options, android.R.layout.simple_spinner_item);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        model.setAdapter(modelAdapter);

        ArrayAdapter<CharSequence> fuelTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.fuel_type_options, android.R.layout.simple_spinner_item);
        fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelType.setAdapter(fuelTypeAdapter);

        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(this,
                R.array.brand_options, android.R.layout.simple_spinner_item);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brand.setAdapter(brandAdapter);

        ArrayAdapter<CharSequence> notRepairedDamageAdapter = ArrayAdapter.createFromResource(this,
                R.array.not_repaired_damage_options, android.R.layout.simple_spinner_item);
        notRepairedDamageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notRepairedDamage.setAdapter(notRepairedDamageAdapter);
    }
}
