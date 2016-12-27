package com.gmod.gianni.querbeer;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gmod.gianni.querbeer.model.Beer;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback<Beer> {


    public static final String TAG = "BAD.MainActivity";

    private static final String apiKey = "4e575388b47e83690f10637f851fd9ff";
    private static final String format = "json";

    private BeerInterface instance;

    private ProgressBar progressLoading;
    private FloatingActionButton fab;

    private EditText inputText;
    private TextInputLayout inputLayoutText;

    private LinearLayout beerDetails;
    private TextView beerName;
    private TextView beerDescription;
    private TextView type;

    private ImageView beerLogo;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    private void bindViews() {
        //TODO aggiungere butterknife
        progressLoading = (ProgressBar) findViewById(R.id.progress_loading);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        inputLayoutText = (TextInputLayout) findViewById(R.id.input_layout_text);
        inputText = (EditText) findViewById(R.id.input_text);
        beerDetails = (LinearLayout) findViewById(R.id.beer_details);
        beerName = (TextView) findViewById(R.id.beer_name);
        beerDescription = (TextView) findViewById(R.id.beer_description);
        beerLogo = (ImageView) findViewById(R.id.beer_logo);
        type = (TextView) findViewById(R.id.beer_type);
    }

    private boolean validateText() {
        if (inputText.getText().toString().trim().isEmpty()) {
            inputLayoutText.setError(getString(R.string.err_msg_text));
//            requestFocus(inputText);
            return false;
        } else {
            inputLayoutText.setErrorEnabled(false);
        }

        return true;
    }

    private void searchBeer(String query) {
        //fare richiesta http con l'url random e stampare tutto quello che ti da come schermata iniziale
        //mettere anche la barra di caricamento on qui.

        progressLoading.setVisibility(View.VISIBLE);
        beerDetails.setVisibility(View.GONE);


        instance.searchForBeer( "search", query, "beer", apiKey, format).enqueue(this);
        Log.d(TAG, "GET SENDED:" + query);
    }

//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE);
//        }
//    }

    private BeerInterface buildBeerInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.brewerydb.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //reflection
        return retrofit.create(BeerInterface.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindViews();
        instance = buildBeerInstance();


        fab.setOnClickListener(this);

        searchBeer("Peroni");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fab) {
            if (!validateText()) {
                return;
            }

            Snackbar.make(v, "Searching beer, please wait!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            progressLoading.setVisibility(View.VISIBLE);
            beerDetails.setVisibility(View.GONE);

            String beerQuery = inputText.getText().toString();
            searchBeer(beerQuery.toLowerCase().trim());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Call<Beer> call, Response<Beer> response) {
        Beer result = response.body();

        Log.d(TAG, "STATUS RESPONSE:" + response.headers());


        if (result == null || result.getData() == null || result.getData().isEmpty()) {
            Toast.makeText(MainActivity.this, "Beer not founded",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "results equals to null");
            return;
        }

        //Picasso
        if(result.getData().get(0).getLabels() != null){

            Picasso.with(this)
                    .load(result.getData().get(0).getLabels().getMedium())
                    .into(beerLogo);
        }else{
            beerLogo.setImageResource(R.drawable.ic_demo);
        }


        if (!result.getStatus().trim().toLowerCase().contains("success")) {

            Toast.makeText(MainActivity.this, "Bad Request!",
                    Toast.LENGTH_SHORT).show();

            Log.d(TAG, "received results not succeded: bad request");

            return;
        }


        String description = result.getData().get(0).getDescription();
        String typeName = result.getData().get(0).getStyle().getName();
        String abv = result.getData().get(0).getAbv();
        String name = result.getData().get(0).getName();

        if (name == null || name.contentEquals("null")) {
            name = "NONAME beer";
        }

        if (description == null || description.contentEquals("null")) {
            description = "";
        }

        if (typeName == null || typeName.contentEquals("null")) {
            description = "";
        }

        if (abv == null || abv.contentEquals("null")) {
            abv = "";
        } else {
            abv = " (" + abv + "%)";
        }

        //ITERARE SUI RISULTATI (TROVARE UN MODO PER SWIFTARE
        beerName.setText(name);
        beerDescription.setText(description);
        type.setText(String.format("%s%s", typeName, abv));

        progressLoading.setVisibility(View.GONE);
        beerDetails.setVisibility(View.VISIBLE);

        Log.d(TAG, result.getData().get(0).getName() + "-" + result.getData().get(0).getName());
    }

    private void handleError(Throwable e) {
        Log.d(TAG, "Runtime error during Beer download", e);
        progressLoading.setVisibility(View.GONE);

        Toast.makeText(MainActivity.this, "Connection Refused!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Call<Beer> call, Throwable t) {
        handleError(t);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}