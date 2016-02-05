package imprika.stockbucks;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

public class GcmRegistrationService extends IntentService {

    public GcmRegistrationService() {
        super("GcmRegistrationService");}

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharePref= getSharedPreferences("TokenCheck", this.MODE_PRIVATE);

        if(sharePref.getBoolean("Subscribe", true) == true) {
            try {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                    GcmPubSub pubSub = GcmPubSub.getInstance(this);
                    pubSub.subscribe(token, "/topics/StockBucks", null);
                    sharePref.edit().putBoolean("SentTokenToServer", true).apply();

            } catch (Exception e) {
                e.printStackTrace();
                sharePref.edit().putBoolean("SentTokenToServer", false).apply();
            }
        }
    }
}
