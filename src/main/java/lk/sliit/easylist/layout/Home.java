package lk.sliit.easylist.layout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import lk.sliit.easylist.R;

//import com.google.cloud.translate.Translate;
//import com.google.cloud.translate.Translate.TranslateOption;
//import com.google.cloud.translate.TranslateOptions;
//import com.google.cloud.translate.Translation;

public class Home extends AppCompatActivity {

    private TextView txtViewSearchResult;
    private Button btnTranslate;
    private  TextView txtTranslated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        txtViewSearchResult = (TextView) findViewById(R.id.txtViewSearch);
        btnTranslate = findViewById(R.id.buttonTranslate);
        txtTranslated = findViewById(R.id.textViewTranslated);

//        btnTranslate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Translate translate = TranslateOptions.getDefaultInstance().getService();
//
//                // The text to translate
//                String text = txtViewSearchResult.getText().toString();
//
//                // Translates some text into Russian
//                Translation translation =
//                        translate.translate(
//                                text,
//                                TranslateOption.sourceLanguage("si"),
//                                TranslateOption.targetLanguage("en"));
//
//                txtTranslated.setText(translation.getTranslatedText());
//            }
//        });

    }

    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(intent, 10);
        }else{
            Toast.makeText(this,"Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 10:
                if(requestCode != RESULT_OK && data !=null){
                    ArrayList<String> result= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtViewSearchResult.setText(result.get(0));
                }
                break;
        }
    }
}
