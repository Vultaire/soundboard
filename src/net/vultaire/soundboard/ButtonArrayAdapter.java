package net.vultaire.soundboard;

import java.io.File;
import java.util.List;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.net.Uri;

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
	String label = segs.get(segs.size()-1);
	SoundButton sb = new SoundButton(
	    (Soundboard)context,
	    (Button)convertView,
	    objects[position]);

	((Button)convertView).setText(label);
	((Button)convertView).setOnClickListener(new ClickListener(sb));
	return convertView;
    }
}
