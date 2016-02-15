package imprika.stockbucks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class Verification extends AppCompatActivity implements View.OnClickListener{

    Button loginButton, registerButton;
    WebView stockTicker, eodChart;
    PendingIntent pi;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        getSupportActionBar().setSubtitle("Bring Home Profits");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);

        loginButton= (Button) findViewById(R.id.login_button);
        registerButton= (Button) findViewById(R.id.register_button);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        stockTicker = (WebView) findViewById(R.id.stock_ticker);
        stockTicker.getSettings().setJavaScriptEnabled(true);
        stockTicker.setHorizontalScrollBarEnabled(false);
        stockTicker.setVerticalScrollBarEnabled(false);
        stockTicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        stockTicker.setVisibility(View.INVISIBLE);
        stockTicker.loadUrl("http://www.indianotes.com/widgets/indices-ticker/index.php?type=indices-ticker&w=350");
        stockTicker.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                stockTicker.loadUrl("javascript:(function(){document.getElementsByClassName('heading')[0].style.display='none';" +
                        "document.getElementsByTagName(\"div\")[3].style.display='none';" +
                        "document.getElementsByTagName(\"div\")[4].style.display='none';" +
                        "document.getElementById(\"myQuote\").parentNode.style.display='none';" +
                        "document.getElementsByTagName(\"div\")[0].style.border='none';})()");
                stockTicker.setVisibility(View.VISIBLE);
            }
        });

        eodChart = (WebView) findViewById(R.id.eod_chart);
        eodChart.getSettings().setUseWideViewPort(true);
        eodChart.getSettings().setLoadWithOverviewMode(true);
        eodChart.loadUrl("http://chartfeed.icharts.in/ShowChart.php?chartkey=e01a8e64cbdba0bf2e6169879a55bf4e&symbol=NIFTY&pr_period=3M&uind1=EMA&uind1_param=5&uind2=BB&uind2_param=20,2&lind1=RSI&lind1_param=2&lind2=CCI&lind2_param=4&lind3=MACD&lind3_param=3,10,16%22%20border=%220%22%20alt=%22Nifty%20EOD%20Charts");

        Intent i = new Intent("ModifyDatabase");
        pi = PendingIntent.getBroadcast(this, 0, i, 0);
        startAlarm();
    }

    public void startAlarm() {

        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 10000;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, interval, pi);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_LONG).show();
        Log.e("Success:", "Alarm Set!");
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.login_button:
                startActivity(new Intent(this, DashBoard.class));
                break;

            case R.id.register_button:
                startActivity(new Intent(this, Subscription.class));
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_verification_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case R.id.how:
                startActivity(new Intent(this, HowItWorks.class));
                break;
            case R.id.performance:
                startActivity(new Intent(this, Performance.class));
                break;
        }
        return true;
    }
}
