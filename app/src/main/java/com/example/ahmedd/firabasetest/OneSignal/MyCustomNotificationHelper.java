package com.example.ahmedd.firabasetest.OneSignal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ahmedd.firabasetest.MessageActivity;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class MyCustomNotificationHelper  implements OneSignal.NotificationOpenedHandler {

    Context ApplicationContext;
   public static String userYouChatWith;

    public MyCustomNotificationHelper( Context ApplicationContext) {
        this.ApplicationContext=ApplicationContext;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;

        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
        OneSignal.getTags(new OneSignal.GetTagsHandler() {
            @Override
            public void tagsAvailable(JSONObject tags) {
                try {
                    userYouChatWith = tags.get("User ID").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Intent intent = new Intent(ApplicationContext, MessageActivity.class);
        intent.putExtra("userID", userYouChatWith);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        ApplicationContext.startActivity(intent);

        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);

        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //   if you are calling startActivity above.
     /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     */
    }
}
