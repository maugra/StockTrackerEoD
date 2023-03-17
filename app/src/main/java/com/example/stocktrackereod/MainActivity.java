package com.example.stocktrackereod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stocktrackereod.databinding.ActivityMainBinding;
import com.example.stocktrackereod.portfolio.Portfolio;
import com.example.stocktrackereod.position.Position;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String BASE_URL = "https://api.polygon.io/v2/aggs/ticker/";
    private final String API_KEY = "2guV5i8O4ZqgNchftZ1WvhIbAqJMPRLf";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private  Portfolio portfolio = new Portfolio();
    RequestQueue requestQueue;
    private PositionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestQueue = Volley.newRequestQueue(this);
        setSupportActionBar(binding.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        RecyclerView positionsList = findViewById(R.id.positions_list);
        portfolio = retrievePortfolio();
        setupAdapter(positionsList);
        updatePortfolio();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        binding.addNewPosition.setOnClickListener(view -> {
            navController.navigate(R.id.addSymbolFragment);
        });
        vibrate();
    }

    private Portfolio retrievePortfolio() {
        if (getPortfolioFromSharedPreferences(this.getBaseContext()) != null) {
            return getPortfolioFromSharedPreferences(this.getBaseContext());
        }
        return new Portfolio();
    }

    private void setupAdapter(RecyclerView positionsList) {
        adapter = new PositionsAdapter(portfolio.getPositions());
        positionsList.setAdapter(adapter);
        positionsList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onStop() {
        super.onStop();
        savePortfolioToSharedPreferences(this.getBaseContext(), portfolio);
    }

    public void getPriceForPosition(Position position) {
        requestQueue.add(buildGetPriceRequest(position));
        requestQueue.start();
    }

    private JsonObjectRequest buildGetPriceRequest(Position position) {
        JSONObject jsonObject = new JSONObject();
        return new JsonObjectRequest(Request.Method.GET, constructURL(position.getSymbol()), jsonObject,
                response -> {
                    try {
                        double price = extractClosingPrice(response);
                        position.updateValueAndDifferential(price);
                        processNewValues();

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    System.out.println("That didn't work");
                });
    }

    private double extractClosingPrice(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONArray("results").getJSONObject(0).getDouble("c");
    }

    private String constructURL(String symbol) {
        return BASE_URL + symbol + "/prev?adjusted=true&apiKey=" + API_KEY;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void updatePortfolio() {
        List<Position> positions = this.portfolio.getPositions();
        positions.forEach(this::getPriceForPosition);
    }

    private void processNewValues() {
        this.portfolio.updateValueAndDifferential();
        TextView portfolioValue =  findViewById(R.id.portfolio_value);
        TextView portfolioDiff = findViewById(R.id.portfolio_diff);
        String portfolioValueText = String.format("%.2f", portfolio.getCurrentPortfolioValue()) + " USD";

        portfolioValue.setText(portfolioValueText);
        String portfolioDifferential =  String.format("%.2f", portfolio.getPortfolioDifferential())  + "%";
        portfolioDiff.setText(portfolioDifferential);
        dataSetChanged();

    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void dataSetChanged() {
        this.adapter.notifyDataSetChanged();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        VibrationEffect vibrationEffect = VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.vibrate(vibrationEffect);
    }

    public void savePortfolioToSharedPreferences(Context context, Portfolio portfolio) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonPortfolio = gson.toJson(portfolio);
        editor.putString("portfolio", jsonPortfolio);
        editor.apply();
    }

    public Portfolio getPortfolioFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("portfolio", null);
        Gson gson = new Gson();
        return gson.fromJson(json, Portfolio.class);
    }

    public PositionsAdapter getAdapter(){
        return this.adapter;
    }

}
