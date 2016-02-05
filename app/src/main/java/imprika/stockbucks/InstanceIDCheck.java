package imprika.stockbucks;

import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.gms.iid.InstanceIDListenerService;

public class InstanceIDCheck extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        SharedPreferences SharePre= getSharedPreferences("TokenCheck", this.MODE_PRIVATE);
        if(SharePre.getBoolean("Subscribe", false)== true) {
        startService(new Intent(this, GcmRegistrationService.class));}
        else{this.stopSelf();}
    }
}
