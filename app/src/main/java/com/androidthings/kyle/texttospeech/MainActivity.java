package com.androidthings.kyle.texttospeech;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Context thisThing = this;
    Button speakButton;
    TextView speakBox;
    public static int TTS_DATA_CHECK = 1;
    public static int VOICE_RECOGNITION = 2;
    private TextToSpeech tts, ttsagain;
    private boolean ttsIsInit, ttsagainIsInit = false;
    public String demotext = "This is a test of the text-to-speech engine in Android.";
    boolean ttsUnder20Flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speakButton = (Button) findViewById(R.id.ConvertButton);
        speakBox = (TextView) findViewById(R.id.textToConvertBox);
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTTS(v);
                demotext = speakBox.getText().toString();
                Log.d("Box is empty", demotext.isEmpty()+"");
                if(demotext.isEmpty()){
                    demotext = "The text box is empty. Please type something first!";
                    Toast.makeText(thisThing, "The text box is empty. Please type something first!", Toast.LENGTH_LONG);
                }
                if (ttsUnder20Flag) {
                    ttsUnder20(demotext);
                } else {
                    ttsGreater21(demotext);
                }
            }
        });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener(){

            @Override
            public void onInit(int status) {
                if (tts.isLanguageAvailable(Locale.US) >= 0) {
                    tts.setLanguage(Locale.US);
                    tts.setPitch(0.8f);
                    tts.setSpeechRate(1.1f);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ttsGreater21(demotext);
                    } else {
                        ttsUnder20(demotext);
                    }
                }
            }
        });
    }
    public void testTTS(View v){
        Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, TTS_DATA_CHECK);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TTS_DATA_CHECK) {
            Log.i("SpeechDemo", "## INFO 01: RequestCode TTS_DATA_CHECK = " + requestCode);
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Log.i("SpeechDemo", "## INFO 03: CHECK_VOICE_DATA_PASS");
            } else {
                Log.i("SpeechDemo", "## INFO 04: CHECK_VOICE_DATA_FAILED, resultCode = " + resultCode);
                Log.i("SpeechDemo", "## INFO 04: CHECK_VOICE_DATA_FAILED, resultCode = " + resultCode);
                Intent installVoice = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installVoice);
            }
        } else if (requestCode == VOICE_RECOGNITION) {
            Log.i("SpeechDemo", "## INFO 02: RequestCode VOICE_RECOGNITION = " + requestCode);
        } else {
            Log.i("SpeechDemo", "## ERROR 01: Unexpected RequestCode = " + requestCode);
        }

    }
    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        ttsUnder20Flag = true;
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        ttsUnder20Flag = false;
        String utteranceId=this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        // TODO Auto-generated method stub
        super.onDestroy();
        if (ttsIsInit) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        finish();
    }
}
