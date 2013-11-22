package net.vultaire.soundboard;

import java.io.File;
import android.widget.Button;
import android.media.MediaPlayer;


class SoundButton {

    public Button button;
    public File file;
    private Soundboard sb;
    private boolean playing;
    private MediaPlayer mp;

    SoundButton(Soundboard sb, Button button, File file) {
	this.sb = sb;
	this.button = button;
	this.file = file;
	this.playing = false;
    }

    public void toggle() {
	if (!playing) {
	    play();
	} else {
	    stop();
	}
    }

    public void play() {
	if (mp != null) {
	    mp.release();
	}
	playing = true;
	button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.stop, 0, 0);
	mp = sb.playFile(this);
    }

    public void stop() {
	if (mp != null) {
	    mp.release();
	}
	button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.play, 0, 0);
	playing = false;
    }
}
