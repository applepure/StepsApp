package com.simpler.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.simpler.myapplication.listeners.ResponseListener;
import com.simpler.myapplication.model.JsonCommentsModel;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

public class StartActivity extends AppCompatActivity implements Serializable {
    ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> validateTextFields(findViewById(R.id.editTextUpperBound),
                                       findViewById(R.id.editTextLowerBound)));
    }

    private void validateTextFields(TextInputEditText upper, TextInputEditText lower) {

        if (upper.getText() == null || upper.getText().toString().equals("")){
            Snackbar.make(upper, R.string.fill_upper_text_field, Snackbar.LENGTH_LONG).show();
            return;
        }
        if (lower.getText() == null || lower.getText().toString().equals("")){
            Snackbar.make(upper, R.string.fill_lower_text_field, Snackbar.LENGTH_LONG).show();
            return;
        }
        if (Integer.parseInt(lower.getText().toString()) > Integer.parseInt(upper.getText().toString())){
            Snackbar.make(upper, R.string.choose_lower_number, Snackbar.LENGTH_LONG).show();
            return;
        }
        String lBound = lower.getText().toString();
        String uBound = upper.getText().toString();
        int lowerInt = Integer.parseInt(lBound);
        int upperInt = Integer.parseInt(uBound);
        if (upperInt - lowerInt < 11){ // making diff always 11 so that the list will be visible and scrollable
            int diff = upperInt - lowerInt;
            int diff1 = 11-diff;
            upperInt = upperInt + diff1;
        }

        makeRequestAndShowProgress(new int[]{lowerInt, upperInt});

    }

    private void makeRequestAndShowProgress(int[] bounds) {

        ResponseListener responseListener = new ResponseListener() {
            @Override
            public void onResponse(List<JsonCommentsModel> response) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    intent.putExtra(getString(R.string.comments), (Serializable) response);
                    startActivity(intent);

            }
        };

       Handler handler = new Handler();
       RequestManager requestManager = new RequestManager(responseListener);
       requestManager.start(bounds, handler);
       pd = new ProgressDialog(StartActivity.this);
       pd.setMessage(getString(R.string.loading_comments));



        pd.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
            requestManager.cancelRequest(); // cancel request
            pd.dismiss();// dismiss dialog
            handler.removeCallbacksAndMessages(null); // dismiss request handler
        });

        pd.show();

    }

    @Override
    protected void onResume() {
        if (pd!= null){
            pd.dismiss();
        }
        super.onResume();
    }
}