package joakim.app.data;

import android.graphics.Color;

public class ColorHandler {

	//converts our input intColor color into an alpha intColor
	public static final int convertColorAlpha(int combinedColor){
	    int alpha = 50;
	    int reds = Color.red(combinedColor);
	    int greens = Color.green(combinedColor);
	    int blues = Color.blue(combinedColor);
	    
		return Color.argb(alpha, reds, greens, blues);
	}
}
