package net.vultaire.soundboard;

import android.view.View;

class ClickListener implements View.OnClickListener {
    SoundButton soundButton;
    ClickListener(SoundButton soundButton) {
	this.soundButton = soundButton;
    }
    public void onClick(View v) {
	soundButton.toggle();
    }
}
