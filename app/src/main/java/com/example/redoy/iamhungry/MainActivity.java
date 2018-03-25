package com.example.redoy.iamhungry;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view_home)
    public RecyclerView homeRecyclerView;
    public AutoFitGridLayoutManager layoutManager;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    String resultText;
    Dialog voiceSearch;
    RecyclerViewAdapterHome adapterHome;
    ArrayList<String> searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeData();
        showCustomDialog();
    }

    private void showCustomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        dialog.setTitle("Voice Search");

        Button dialogButtonCancel = dialog.findViewById(R.id.customDialogCancel);
        Button dialogButtonOk = dialog.findViewById(R.id.customDialogOk);

        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showSearchDialog();
            }
        });

        dialog.show();
    }

    private void showSearchDialog() {
        voiceSearch = new Dialog(this);
        voiceSearch.setContentView(R.layout.custom_dialog_search);
        voiceSearch.setTitle("Voice Search");

        ImageButton btnSpeak = voiceSearch.findViewById(R.id.btnSpeak);
        Button dialogButtonCancel = voiceSearch.findViewById(R.id.customDialogCancel);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceSearch.dismiss();
            }
        });

        voiceSearch.show();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    resultText = result.get(0);
                    Toast.makeText(getApplicationContext(), resultText, Toast.LENGTH_LONG).show();
                    adapterHome.getFilter().filter(resultText);
                    voiceSearch.dismiss();
                }
                break;
            }
        }
    }

    private void initializeData() {
        searchResult = getIntent().getStringArrayListExtra("results");

        ArrayList<HomeItem> rowListItem = getAllItemList(searchResult);
        adapterHome = new RecyclerViewAdapterHome(getApplicationContext(), rowListItem, getSupportFragmentManager());
        homeRecyclerView.setAdapter(adapterHome);

        layoutManager = new AutoFitGridLayoutManager(getApplicationContext(), 500);
        homeRecyclerView.setLayoutManager(layoutManager);
    }

    private ArrayList<HomeItem> getAllItemList(ArrayList<String> searchResult) {

        ArrayList<HomeItem> allItems = new ArrayList<>();
        for (int i = 0; i < searchResult.size(); i++) {
            allItems.add(new HomeItem(searchResult.get(i), R.drawable.a, "#09A9FF"));
        }
        return allItems;
    }
}
