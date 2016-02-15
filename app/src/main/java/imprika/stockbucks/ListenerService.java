package imprika.stockbucks;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmListenerService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListenerService extends GcmListenerService {

        @Override
        public void onMessageReceived(String from, Bundle data) {

                SharedPreferences sharePref = getSharedPreferences("TokenCheck", this.MODE_PRIVATE);
                if (sharePref.getBoolean("Subscribe", false) == true) {
                        String message = data.getString("message");
                        String title = data.getString("title");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("DD-MM-yyyy HH:mm:ss");
                        String currentTimeStamp = dateFormat.format(new Date());
                        DatabaseHelper dh = new DatabaseHelper(this);
                        dh.insertData(currentTimeStamp, message);

                        Intent intent = new Intent(this, DashBoard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                        Intent actionIntent = new Intent(this, Zerodha.class);
                        actionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent actionPendIntent = TaskStackBuilder.create(this).addParentStack(Zerodha.class).addNextIntent(actionIntent).getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message).setBigContentTitle(title).setSummaryText(title))
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                .setPriority(2).setColor(0Xff654856)
                                .setContentIntent(pendingIntent)
                                .addAction(R.drawable.common_google_signin_btn_icon_dark, "Trade", actionPendIntent);

                        int uniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
                        notificationManager.notify(uniqueId, notificationBuilder.build());

                        Intent uiUpdate = new Intent("Update_TextView");
                        uiUpdate.putExtra("message", message);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(uiUpdate);

                }
        }
}
