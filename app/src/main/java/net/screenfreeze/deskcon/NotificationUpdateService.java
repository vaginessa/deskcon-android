package net.screenfreeze.deskcon;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

@SuppressLint("NewApi")
public class NotificationUpdateService extends NotificationListenerService {
	private static SharedPreferences sharedPrefs;
	private NotificationUtils utils;

	@Override
	public void onCreate() {
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		utils = new NotificationUtils(this);
		super.onCreate();
	}

	@Override
	public void onNotificationPosted(StatusBarNotification not) {
		String packagename = not.getPackageName();

		utils.handleNotification(not.getNotification(), packagename);
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification arg0) {
	}
}
