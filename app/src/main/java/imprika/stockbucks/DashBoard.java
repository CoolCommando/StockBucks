package imprika.stockbucks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.iid.InstanceID;

import java.util.zip.Inflater;


public class DashBoard extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    static final String[]   NAVIGATION_ITEMS =new String[] {"Order Status", "Performance", "Payments", "How it works?", "Contact us", "Profile Settings", "Logout"};
    ListView listView;
    WebView stockTicker, eodChart, kitePublisher;
    Button tradeButton;
    View zerodhaView;
    TextView tradeAdvice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        tradeAdvice = (TextView) findViewById(R.id.trade_advice);
        LocalBroadcastManager.getInstance(this).registerReceiver(updateTextView, new IntentFilter("Update_TextView"));

        SharedPreferences tradeAdvicePref = getSharedPreferences("Trade_Advice", this.MODE_PRIVATE);
        tradeButton= (Button) findViewById(R.id.trade_button);
        tradeButton.setVisibility(View.INVISIBLE);
        tradeButton.setEnabled(false);

        if(tradeAdvicePref.getString("Today's_Advice", "Nothing Yet!!!") != "Nothing Yet!!!"){
            tradeAdvice.setText(tradeAdvicePref.getString("Today's_Advice", "Nothing yet"));
            tradeButton.setVisibility(View.VISIBLE);
            tradeButton.setEnabled(true);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){notificationManager.cancelAll();}

        SharedPreferences SharePre= getSharedPreferences("TokenCheck", this.MODE_PRIVATE);
        SharePre.edit().putBoolean("Subscribe", true).apply();

        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);

        if(checkPlayServices()){
            startService(new Intent(this, GcmRegistrationService.class));
        }

        listView= (ListView) findViewById(R.id.list_view);
        ArrayAdapter<String> aAdapter= new ArrayAdapter<String>(this, R.layout.custom_listview, NAVIGATION_ITEMS);
        listView.setAdapter(aAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        startActivity(new Intent(getApplicationContext(), OrdersHistory.class));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 1:
                        startActivity(new Intent(getApplicationContext(), Performance.class));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 2:
                        startActivity(new Intent(getApplicationContext(), Payments.class));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 3:
                        startActivity(new Intent(getApplicationContext(), HowItWorks.class));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 4:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 5:
                        startActivity(new Intent(getApplicationContext(), ProfileSettings.class));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 6:
                        alert();
                }
            }
        });

        zerodhaView = View.inflate(DashBoard.this, R.layout.zerodha_login, null);
        kitePublisher = (WebView) zerodhaView.findViewById(R.id.kite_publisher);
        kitePublisher.getSettings().setJavaScriptEnabled(true);
        kitePublisher.setHorizontalScrollBarEnabled(false);
        kitePublisher.setVerticalScrollBarEnabled(false);
        kitePublisher.setWebViewClient(new WebViewClient());
        kitePublisher.loadUrl("https://kite.zerodha.com/");

        final Dialog confirmTrade = new Dialog(this);
        confirmTrade.setContentView(zerodhaView);

        tradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.trade_button) {
                    confirmTrade.show();
                }
            }
        });

        stockTicker = (WebView) findViewById(R.id.stock_ticker_dashboard);
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

        eodChart = (WebView) findViewById(R.id.eod_chart_dashboard);
        eodChart.getSettings().setUseWideViewPort(true);
        eodChart.getSettings().setLoadWithOverviewMode(true);
        eodChart.loadUrl("http://chartfeed.icharts.in/ShowChart.php?chartkey=e01a8e64cbdba0bf2e6169879a55bf4e&symbol=NIFTY&pr_period=3M&uind1=EMA&uind1_param=5&uind2=BB&uind2_param=20,2&lind1=RSI&lind1_param=2&lind2=CCI&lind2_param=4&lind3=MACD&lind3_param=3,10,16%22%20border=%220%22%20alt=%22Nifty%20EOD%20Charts");
    }

  @Override
  public boolean onOptionsItemSelected(MenuItem item){
      if(item.getItemId() == android.R.id.home){

          if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
              mDrawerLayout.closeDrawer(GravityCompat.START);
          } else {
              mDrawerLayout.openDrawer(GravityCompat.START);
          }
      }
      return true;
  }

    @Override
    public void onBackPressed(){

        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateTextView);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, 5000).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    public void cancelNotifications(){

       final InstanceID instanceID = InstanceID.getInstance(this);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    instanceID.deleteInstanceID();
                    SharedPreferences sharePref= getSharedPreferences("TokenCheck", getApplicationContext().MODE_PRIVATE);
                    sharePref.edit().putBoolean("SentTokenToServer", false).apply();
                }
                 catch (Exception e) {
                    e.printStackTrace(); } } });
        thread.start();
        Toast.makeText(this, "Unsubscribed", Toast.LENGTH_LONG).show();
        finish();
    }

    public void alert(){

        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.CustomAlertDialog);
        AlertDialog.Builder confirmLogout= new AlertDialog.Builder(ctw);
        confirmLogout.setTitle("Logout?")
        .setMessage("Please note that if you log out, you will be unsubscribed from our daily trade advices and will not receive them until you log back in. Are you sure you want to log out?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelNotifications();
                dialog.cancel();
            }
        });
        confirmLogout.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        confirmLogout.setCancelable(false);
        AlertDialog dial= confirmLogout.create();
        dial.show();
        Button yesbtn= dial.getButton(DialogInterface.BUTTON_POSITIVE);
        yesbtn.setAllCaps(false);
        Button nobtn= dial.getButton(DialogInterface.BUTTON_NEGATIVE);
        nobtn.setAllCaps(false);
    }

    private BroadcastReceiver updateTextView= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            tradeAdvice.setText(intent.getExtras().getString("message"));
            tradeButton.setVisibility(View.VISIBLE);
            tradeButton.setEnabled(true);
        }
    };
    }








