package net.vultaire.soundboard;

import android.app.Activity;
import android.os.Bundle;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class Soundboard extends Activity
{
    private AudioManager am = null;
    private OnAudioFocusChangeListener afChangeListener = null;
    private MediaPlayer mp = null;

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
    }

    @Override
    public void onResume() {
	startAudio();
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

	class MyCompletionListener implements OnCompletionListener {
	    private Soundboard sb;
	    public MyCompletionListener(Soundboard sb) {
		this.sb = sb;
	    }
	    public void onCompletion(MediaPlayer mp) {
		sb.releaseAudio();
	    }
	}

	mp = MediaPlayer.create(this, R.raw.trololo);
	mp.setOnCompletionListener(new MyCompletionListener(this));
	mp.start();
    }

    public void releaseAudio() {
	if (mp != null) {
	    mp.release();
	    mp = null;
	}
	am.abandonAudioFocus(afChangeListener);
    }
}
