package net.vultaire.soundboard;

import android.app.Activity;
import android.os.Bundle;
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
import android.view.LayoutInflater;

public class Soundboard extends Activity
{
    private AudioManager am;
    private OnAudioFocusChangeListener afChangeListener;
    private MediaPlayer mp;

    class ButtonArrayAdapter extends ArrayAdapter<String> {
	Context context;
	int resource;
	String[] objects;
	ButtonArrayAdapter(Context context, int resource, String[] objects) {
	    super(context, resource, objects);
	    this.context = context;
	    this.resource = resource;
	    this.objects = objects;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
	    if (convertView == null) {
		/* Should we reuse the view?  How can we even do this
		 * in the first place; aren't we seeing different
		 * instances of the view when in a list/grid?  And
		 * what about events...  If we have a list of buttons,
		 * can they all respond differently if we reuse the
		 * view? */
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		/* Not *completely* sure on this command.  Should I
		 * use parent?  Should I use null?  Not fully
		 * understanding the difference yet. */
		convertView = inflater.inflate(resource, null);
	    }
	    ((Button)convertView).setText(objects[position]);
	    return convertView;
	}
    }

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
	String[] data = {"Trololo", "Shut up, Beavis!"};
	ArrayAdapter adapter = new ButtonArrayAdapter(this, R.layout.button, data);
	GridView gridView = (GridView) findViewById(R.id.grid_view);
	gridView.setAdapter(adapter);
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
