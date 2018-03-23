package com.example.peter.idletimeout.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.peter.idletimeout.MainActivity;


public class MyBaseActivity extends AppCompatActivity {

	public static final long DISCONNECT_TIMEOUT = 200; // 30 sec = 30 * 1000 ms

	private Handler disconnectHandler = new Handler() {
		public void handleMessage(Message msg) {
		}
	};

	private Runnable disconnectCallback = new Runnable() {
		@Override
		public void run() {

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					MyBaseActivity.this);
			alertDialog.setCancelable(false);
			alertDialog.setTitle("Session Timeout");
			alertDialog
					.setMessage("Idle For 5 seconds. Session Closes Automatically");
			alertDialog.setNegativeButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(MyBaseActivity.this,
									MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);

							dialog.cancel();
						}
					});

			alertDialog.show();

			// Perform any required operation on disconnect
		}
	};

	public void resetDisconnectTimer() {
		disconnectHandler.removeCallbacks(disconnectCallback);
		disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
	}

	public void stopDisconnectTimer() {
		disconnectHandler.removeCallbacks(disconnectCallback);
	}

	@Override
	public void onUserInteraction() {
		resetDisconnectTimer();
	}

	@Override
	public void onResume() {
		super.onResume();
		resetDisconnectTimer();
	}

	@Override
	public void onStop() {
		super.onStop();
		stopDisconnectTimer();
	}
}
