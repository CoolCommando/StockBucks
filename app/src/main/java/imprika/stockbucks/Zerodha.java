package imprika.stockbucks;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Zerodha extends AppCompatActivity {

    WebView zerodhaKite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zerodha);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){notificationManager.cancelAll();}

        zerodhaKite = (WebView) findViewById(R.id.zerodha_kite);
        zerodhaKite.getSettings().setJavaScriptEnabled(true);
        zerodhaKite.setHorizontalScrollBarEnabled(false);
        zerodhaKite.setVerticalScrollBarEnabled(false);
        zerodhaKite.setWebViewClient(new WebViewClient());
        zerodhaKite.loadUrl("https://kite.zerodha.com/");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}
