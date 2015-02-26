package net.screenfreeze.deskcon;

import java.io.ByteArrayOutputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationUpdateServiceOld extends AccessibilityService {
	private static SharedPreferences sharedPrefs;

	@Override
	public void onCreate() {
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		super.onCreate();
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
			Notification not = (Notification) event.getParcelableData();


			// permissions
			boolean send_other_notifications = sharedPrefs.getBoolean("send_other_notifications", false);
			ArrayList<String> whitelist = getNotificationWhitelist();
			String packagename = String.valueOf(event.getPackageName());

			if (not != null && send_other_notifications && whitelist.contains(packagename)) {
				Log.d("Notification: ", "new post");

				if (android.os.Build.VERSION.SDK_INT >= 19) {
					String packageName = getAppnameFromPackagename(packagename);

					Bundle extras = not.extras;
					if (extras.containsKey(Notification.EXTRA_TEMPLATE)) {
						String template = extras.getString(Notification.EXTRA_TEMPLATE);
						if (template.equals(Notification.BigTextStyle.class)) {
							String title = extras.getString(Notification.EXTRA_TITLE_BIG);
							if (title == null || title.isEmpty()) title = extras.getString(Notification.EXTRA_TITLE);
							String text = extras.getString(Notification.EXTRA_BIG_TEXT);
							if (text == null || text.isEmpty()) text = extras.getString(Notification.EXTRA_TEXT);
							String[] people = (String[]) extras.get(Notification.EXTRA_PEOPLE);

							Bitmap icon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON_BIG);
							if (icon == null) icon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
							if (icon == null) icon = (Bitmap) extras.get(Notification.EXTRA_SMALL_ICON);

							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
							startUpdateServiceCommand(packageName, title, text, stream);
							return;
						}
					}
				}
				if (not.tickerText != null) {
					String text = not.tickerText.toString();
					startUpdateServiceCommand(text);
					return;
				}
			}
		}
	}

	@Override
	protected void onServiceConnected() {
		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
		info.notificationTimeout = 1;
		info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
		setServiceInfo(info);
	}

	//send specific Command
	private void startUpdateServiceCommand(String mess) {
		Intent i = new Intent(getBaseContext(), StatusUpdateService.class);
		i.putExtra("commandtype", "OTH_NOT");
		i.putExtra("message", mess);

		startService(i);
	}

	//send specific Command
	private void startUpdateServiceCommand(String appName, String title, String text, ByteArrayOutputStream icon) {
		Intent i = new Intent(getBaseContext(), StatusUpdateService.class);
		JSONObject data = new JSONObject();
		try {
			data.put("appName", appName);
			data.put("title", title);
			data.put("text", text);
			data.put("icon", Base64.encodeToString(icon.toByteArray(), Base64.DEFAULT));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		i.putExtra("commandtype", "NOT_BIG");
		i.putExtra("message", data.toString());

		startService(i);
	}

	private ArrayList<String> getNotificationWhitelist() {
		String whiteliststr = sharedPrefs.getString("notification_whitelist", "");
		ArrayList<String> whitelist = new ArrayList<String>();
		if (whiteliststr.equals("")) {
			whitelist = new ArrayList<String>();
		} else {
			whitelist = new ArrayList<String>(Arrays.<String>asList((whiteliststr.split(", "))));
		}

		return whitelist;
	}

	private String getAppnameFromPackagename(String pname) {
		PackageManager packageManager = this.getPackageManager();

		try {
			ApplicationInfo app = packageManager.getApplicationInfo(pname, 0);

			//Drawable icon = packageManager.getApplicationIcon(app);
			String name = packageManager.getApplicationLabel(app).toString();

			return name;
		} catch (PackageManager.NameNotFoundException e) {
			return "";
		}
	}

	@Override
	public void onInterrupt() {
	}

}
