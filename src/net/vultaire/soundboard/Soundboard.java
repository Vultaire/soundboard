package net.vultaire.soundboard;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.GridView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.net.Uri;

public class Soundboard extends Activity
{
    private AudioManager am;
    private OnAudioFocusChangeListener afChangeListener;
    private ArrayList<SoundButton> sbs = new ArrayList<SoundButton>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	setVolumeControlStream(AudioManager.STREAM_MUSIC);
	afChangeListener = new OnAudioFocusChangeListener() {
	    public void onAudioFocusChange(int focusChange) {}
	};

	File[] files = {};
	String state = Environment.getExternalStorageState();
	if (externalMediaOk()) {
	    files = getSoundboardDir().listFiles();
	}
	ArrayAdapter adapter = new ButtonArrayAdapter(this, R.layout.button, files);
	GridView gridView = (GridView) findViewById(R.id.grid_view);
	gridView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
	startAudio();
	// File f = new File(
	//     new File(
	//         Environment.getExternalStorageDirectory().getAbsoluteFile(),
	// 	"Soundboard"),
	//     "trololo.mp3");
	// playFile(f);
	super.onResume();
    }

    @Override
    public void onPause() {
	releaseAudio();
	super.onPause();
    }

    @Override
    public void onStop() {
	releaseAudio();
	super.onStop();
    }

    public void startAudio() {
	am = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
	am.requestAudioFocus(afChangeListener,
			     AudioManager.STREAM_MUSIC,
			     AudioManager.AUDIOFOCUS_GAIN);
    }

    public MediaPlayer playFile(SoundButton soundButton) {
	sbs.add(soundButton);
	MediaPlayer mp = MediaPlayer.create(this, Uri.fromFile(soundButton.file));
	mp.setOnCompletionListener(new CompletionListener(soundButton));
	mp.start();
	return mp;
    }

    public void releaseAudio() {
	for (SoundButton sb : sbs) {
	    sb.stop();
	}
	sbs = new ArrayList<SoundButton>();

	if (am != null) {
	    am.abandonAudioFocus(afChangeListener);
	}
    }

    public boolean externalMediaOk() {
	String state = Environment.getExternalStorageState();
	return (Environment.MEDIA_MOUNTED.equals(state)
		|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    public File getSoundboardDir() {
	File dataDir = new File(
	    Environment.getExternalStorageDirectory().getAbsoluteFile(),
	    "Soundboard");
	if (!dataDir.exists()) {
	    dataDir.mkdir();
	}
	return dataDir;
    }
}
