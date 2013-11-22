package net.vultaire.soundboard;

import java.io.File;
import java.util.List;
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
import android.view.LayoutInflater;
import android.net.Uri;

public class Soundboard extends Activity
{
    private AudioManager am;
    private OnAudioFocusChangeListener afChangeListener;
    private ArrayList<MediaPlayer> mps = new ArrayList<MediaPlayer>();

    class ClickListener implements View.OnClickListener {
	File f;
	ClickListener(File f) {
	    this.f = f;
	}
	public void onClick(View v) {
	    playFile(this.f);
	}
    }

    class ButtonArrayAdapter extends ArrayAdapter<File> {
	Context context;
	int resource;
	File[] objects;
	ButtonArrayAdapter(Context context, int resource, File[] objects) {
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
	    List<String> segs = Uri.fromFile(objects[position]).getPathSegments();
	    ((Button)convertView).setText(segs.get(segs.size()-1));
	    ((Button)convertView).setOnClickListener(new ClickListener(objects[position]));
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

    public void playFile(File soundFile) {
	class MyCompletionListener implements OnCompletionListener {
	    public void onCompletion(MediaPlayer mp) {
		mp.release();
	    }
	}

	MediaPlayer mp = MediaPlayer.create(this, Uri.fromFile(soundFile));
	mps.add(mp);
	mp.setOnCompletionListener(new MyCompletionListener());
	mp.start();
    }

    public void releaseAudio() {
	for (MediaPlayer mp : mps) {
	    mp.release();
	    mp = null;
	}
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
