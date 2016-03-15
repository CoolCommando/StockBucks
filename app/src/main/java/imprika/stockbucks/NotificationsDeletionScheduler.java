package imprika.stockbucks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Sujith on 15-02-2016.
 */
public class NotificationsDeletionScheduler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "NotificationsDeletionScheduler fired", Toast.LENGTH_LONG).show();
        Log.e("Success", "Broadcast receiver called!!");
        context.startService(new Intent(context, NotificationsDeleter.class));
    }
}
