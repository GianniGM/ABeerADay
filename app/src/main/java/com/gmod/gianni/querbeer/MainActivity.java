package com.gmod.gianni.querbeer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    private ProgressBar progressLoading;
    private FloatingActionButton fab;

    private EditText inputText;
    private TextInputLayout inputLayoutText;

    private LinearLayout beerDetails;
    private TextView beerName;
    private TextView beerDescription;

    private ImageView beerLogo;

    private void bindVievs(){

        progressLoading = (ProgressBar) findViewById(R.id.progress_loading);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        inputLayoutText = (TextInputLayout) findViewById(R.id.input_layout_text);
        inputText = (EditText) findViewById(R.id.input_text);
        beerDetails =  (LinearLayout) findViewById(R.id.beer_details);
        beerName = (TextView) findViewById(R.id.beer_name);
        beerDescription = (TextView) findViewById(R.id.beer_description);
        beerLogo = (ImageView) findViewById(R.id.beer_logo);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindVievs();

        beerLogo.setImageResource(R.drawable.ic_demo);
        beerName.setText("Orval");
        beerDescription.setText("Birra Trappista belga, ambrata, 6.5 gradi");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateText()){
                    return;
                }


                Snackbar.make(view, "Listing beers, please wait!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                beerDetails.setVisibility(View.VISIBLE);

            }
        });
    }

    private boolean validateText() {
        if (inputText.getText().toString().trim().isEmpty()) {
            inputLayoutText.setError(getString(R.string.err_msg_text));
            requestFocus(inputText);
            return false;
        } else {
            inputLayoutText.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
}
