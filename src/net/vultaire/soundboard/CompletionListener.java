package net.vultaire.soundboard;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

class CompletionListener implements OnCompletionListener {

    SoundButton sb;

    CompletionListener(SoundButton sb) {
	this.sb = sb;
    }

    public void onCompletion(MediaPlayer mp) {
	sb.stop();
    }
}
