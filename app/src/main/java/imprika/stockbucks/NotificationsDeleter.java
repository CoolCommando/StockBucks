package imprika.stockbucks;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Sujith on 15-02-2016.
 */
public class NotificationsDeleter extends IntentService {

    public NotificationsDeleter() {
        super("NotificationsDeleter");}

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e("Success", "IntentSerivce Fired" );
        DatabaseHelper dh = new DatabaseHelper(this);
        dh.deleteTable();

    }
}
