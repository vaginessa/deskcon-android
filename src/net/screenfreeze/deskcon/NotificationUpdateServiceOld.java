package net.screenfreeze.deskcon;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationUpdateServiceOld extends AccessibilityService {
	private static SharedPreferences sharedPrefs;
	private NotificationUtils utils;

	@Override
	public void onCreate() {
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		utils = new NotificationUtils(this);

		super.onCreate();
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
			Notification not = (Notification) event.getParcelableData();

			utils.handleNotification(not, event.getPackageName().toString());
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

	@Override
	public void onInterrupt() {
	}

}
