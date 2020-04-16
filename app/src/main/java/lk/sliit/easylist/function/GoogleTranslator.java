package lk.sliit.easylist.function;

import android.widget.TextView;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import lk.sliit.easylist.R;

public class GoogleTranslator implements Runnable {
    private volatile Translation translation;
    private String text;
    private volatile String translatedText;
    @Override
    public synchronized void run() {
        translate();
    }

    public synchronized  void setText(String text) {
        this.text = text;
    }

    public synchronized  String getTranslatedText() {
        return translatedText;
    }

    public synchronized void translate(){
        Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyDMgOmpjBHkcrRlSFs2Nlvw2N9yJcYrJfw").build().getService();
        // The text to translate
        translation = translate.translate(text, Translate.TranslateOption.targetLanguage("en"));
        this.translatedText=translation.getTranslatedText();
        System.out.println("Translated Text: "+translatedText);
    }
}
