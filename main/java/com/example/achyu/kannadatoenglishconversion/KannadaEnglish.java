package com.example.achyu.kannadatoenglishconversion;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.speech.RecognizerIntent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class KannadaEnglish extends AppCompatActivity {

    private static String API_KEY = "trnsl.1.1.20180430T140829Z.d0fa7ab7d027147f.50f6b0718626f09033b27fe485312d87e4c0b273";
    private static String URL = "https://translate.yandex.net/api/v1.5/tr.json/translate";
    private static final int SPEECH_REQUEST_CODE = 0;

    //Variables

     String language = null;
     String lang=null;
     String sourcetext;

    //UI variables

    TextView mSource;
    TextView mDest;
    Button mTransalte;
    ImageView mVoice;

    Button kanToeng;
    Button engTokan;
    ConstraintLayout cn1;
    ConstraintLayout cn2;
    FrameLayout fr1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kannada_english);
        initializeUI();

        kanToeng = (Button)findViewById(R.id.button8);
        engTokan = (Button)findViewById(R.id.button9);
        cn1 = (ConstraintLayout)findViewById(R.id.cn1);
        cn2 = (ConstraintLayout)findViewById(R.id.cn2);
        fr1 = (FrameLayout)findViewById(R.id.ss) ;

        kanToeng.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cn1.setVisibility(View.INVISIBLE);
                cn2.setVisibility(View.VISIBLE);
                language = "kn-en";
                lang = "kn-IN";
                getSupportActionBar().setTitle("Kannada Translator");
            }
        }));

        engTokan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cn1.setVisibility(View.INVISIBLE);
                cn2.setVisibility(View.VISIBLE);
                language = "en-kn";
                lang = "en-In";
                getSupportActionBar().setTitle("English Translator");
            }
        });




        //listener for button click
        mTransalte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sourcetext= mSource.getText().toString();
                translate();


            }
        });

        mSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                sourcetext= mSource.getText().toString();
                translate();

            }
        });

        mVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Clima:","Button clicked");
                displaySpeechRecognizer();

            }
        });
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{});
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,lang);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, lang);
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, lang);
        intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{"kn"});

// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            sourcetext = results.get(0);
            mSource.setText(sourcetext);
            translate();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //initialization of UI elements
    private void initializeUI(){
        mSource = (TextView) findViewById(R.id.source);
        mDest = (TextView) findViewById(R.id.dest);
        mTransalte =(Button) findViewById(R.id.translation);
        mVoice= (ImageView) findViewById(R.id.voice);

        mSource.requestFocus();
        mDest.setFocusable(false);

    }
    private void translate()
    {
        RequestParams params = new RequestParams();
        params.put("key",API_KEY);
        params.put("text",sourcetext);
        params.put("lang",language);
        params.put("format","text");
        params.put("callback","JSON");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("Clima","Success json "+responseString);

                responseString = responseString.substring(responseString.indexOf("(")+1,responseString.indexOf(")"));

                Log.d("Clima","Success json "+responseString);
                try {
                    JSONObject res = new JSONObject(responseString);

                    Log.d("Clima","Success json "+res.getString("text"));

                    int code = res.getInt("code");
                    if(code == 502)
                    {
                        mDest.clearComposingText();
                    }


                    String result = res.getString("text");
                    result = result.substring(result.indexOf("[")+2,result.indexOf("]")-1);

                    mDest.setText(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }


    public void sss(View view) {
        fr1.setVisibility(View.INVISIBLE);
        cn1.setVisibility(View.VISIBLE);
    }
}

