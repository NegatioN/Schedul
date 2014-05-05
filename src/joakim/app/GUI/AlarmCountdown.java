package joakim.app.GUI;

import android.os.CountDownTimer;
import android.widget.TextView;

public class AlarmCountdown extends CountDownTimer{

	private TextView tv;
	
	public AlarmCountdown(long millisInFuture, long countDownInterval,TextView tv) {
		super(millisInFuture, countDownInterval);
		this.tv = tv;
	}

	@Override
	public void onFinish() {
		 tv.setText("done!");		
	}

	@Override
	public void onTick(long millisUntilFinished) {
		tv.setText("Left: " + millisUntilFinished / 1000);		
	}

}
