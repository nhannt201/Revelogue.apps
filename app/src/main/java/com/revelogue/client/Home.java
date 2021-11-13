package com.revelogue.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Home extends AppCompatActivity {
    BottomNavigationView navigation;
    Toolbar toolbar;
    ShimmerFrameLayout container2;
    ImageView img;
    WebView view;

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activy_home);

        container2 = findViewById(R.id.shimmer_view_container);
        img = findViewById(R.id.logo_change);
        view = findViewById(R.id.browser);
      //  img.set
       img.setImageResource(R.drawable.logo);

        //setup webview
         WebSettings settings =  view.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setDomStorageEnabled(true);
      //  settings.setPluginsEnabled(true);

        view.requestFocus();
        settings.setAppCachePath(this.getFilesDir().getAbsolutePath() + "/cache");
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setWebViewClient(new CustomWebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.setVisibility(View.INVISIBLE);
                container2.setVisibility(View.VISIBLE);
                container2.startShimmer();
                if (isConnected()) {
                    view.loadUrl(url);
                   // view.loadUrl("javascript:(function(){ var youtube = document.getElementsByTagName('iframe'); for(i=0;i<youtube.length;i++) { if(youtube[i].getAttribute('data-src')) { var x = youtube[i].getAttribute('data-src'); youtube[i].setAttribute('src', x); youtube[i].setAttribute('data-src', ''); } } })()");
                   // view.loadDataWithBaseURL(null,"<script>var youtube = document.getElementsByTagName(\"iframe\"); for(i=0;i<youtube.length;i++) { if(youtube[i].getAttribute(\"data-src\")) { var x = youtube[i].getAttribute(\"data-src\"); youtube[i].setAttribute(\"src\", x); youtube[i].setAttribute(\"data-src\", \"\"); } }</script>","text/html","utf-8",null);

                    img.setImageResource(R.drawable.logo);
                } else {
                    img.setImageResource(R.drawable.nointernet);
                    container2.stopShimmer();
                    container2.setVisibility(View.INVISIBLE);
                    reload_auto();
                }
                return true;
            }
            // autoplay when finished loading via javascript injection
            public void onPageFinished(WebView view, String url) {
                view.setVisibility(View.VISIBLE);
                if (isConnected()) {
                    container2.stopShimmer();
                    container2.setVisibility(View.INVISIBLE);
                    //Fix bugs video youtube
                    view.loadDataWithBaseURL("base/url","<h1>loveyouuuuuu</h1>","text/html","utf-8","history/url");
                    //    view.loadUrl("javascript:(function(){ var youtube = document.getElementsByTagName('iframe'); for(i=0;i<youtube.length;i++) { if(youtube[i].getAttribute('data-src')) { var x = youtube[i].getAttribute('data-src'); youtube[i].setAttribute('src', x); youtube[i].setAttribute('data-src', 'no'); } } })()");

                   // view.loadUrl("javascript:(function(){ alert('Xong!'); })()");

                   // view.loadUrl("javascript:(function(){ var youtube = document.getElementsByTagName('iframe'); for(i=0;i<youtube.length;i++) { if(youtube[i].getAttribute('data-src')) { var x = youtube[i].getAttribute('data-src'); youtube[i].setAttribute('src', x); youtube[i].setAttribute('data-src', 'no'); } } })()");

                    // view.setEnabled(true);
                } else {
                    img.setImageResource(R.drawable.nointernet);
                    container2.setVisibility(View.VISIBLE);
                    //  view.setEnabled(false);
                    reload_auto();
                }
               // view.loadDataWithBaseURL(null,"<script>var youtube = document.getElementsByTagName(\"iframe\"); for(i=0;i<youtube.length;i++) { if(youtube[i].getAttribute(\"data-src\")) { var x = youtube[i].getAttribute(\"data-src\"); youtube[i].setAttribute(\"src\", x); youtube[i].setAttribute(\"data-src\", \"\"); } }</script>","text/html","utf-8",null);

            }

            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setMessage(R.string.notification_error_ssl_cert_invalid);
                builder.setPositiveButton(R.string.continuee, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton(R.string.cannell, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }


        });

        //Load web
        loadHome();
        //load menu
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_UNLABELED);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem menuItem) {
                int id = menuItem.getItemId();
             //  Fragment webCato =  new webCato();
                if(id == R.id.navigation_home) {
                    loadHome();
                 //   toolbar.setTitle(getResources().getString(R.string.title_home));
                //    loadFragment(new webHome());
                    return true;
                } else if(id == R.id.navigation_category) {
//                    toolbar.setTitle(getResources().getString(R.string.title_category));
                  //  loadFragment(webCato);
                        //setWebView(;);
                    loadCate();
                    return true;
                } else if(id == R.id.navigation_back ) {
                    //  toolbar.setTitle(getResources().getString(R.string.title_category));
                         if (view.canGoBack()) {
                             view.goBack();
                         }
                    return true;
                }
                return true;
            }
        });


    }

    public void loadHome() {
        if (isConnected()){

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                trustAllHosts();
                view.loadUrl("https://revelogue.com/");
            } else {
                view.loadUrl("https://revelogue.com/");
            }

        } else {
            Toast.makeText(this, R.string.nointernet, Toast.LENGTH_SHORT).show();
            //  view.setEnabled(false);
            img.setImageResource(R.drawable.nointernet);
            container2.stopShimmer();
            reload_auto();

        }
    }

    public void loadCate() {
        String  yourData = "<!DOCTYPE html>\n" +
                "<html lang=\"vi\">\n" +
                "\t<head>\n" +
                "\t\t<title>Chuyên mục</title>\n" +
                "\t\t<meta charset=\"utf-8\">\n" +
                "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "\t\t<style>\n" +
                "\t\timg {\n" +
                "\t\t  border-radius: 25px;\n" +
                "\t\t  height: 210px;\n" +
                "\t\t  width: 90%;\n" +
                "\t\t}" +
                " div {\n" +
                "      width: 100%;\n" +
                "      height: 230px;\n" +
                "      filter: blur(20px);\n" +
                "      background-size: cover;\n" +
                "      animation: hd-quality 1s;\n" +
                "      animation-delay: 3s;\n" +
                "      animation-fill-mode: forwards;\n" +
                "    }\n" +
                "    div::after {\n" +
                "      animation: low-quality 1s;\n" +
                "      animation-delay: 1s;\n" +
                "    }\n" +
                "\t\t@keyframes low-quality {\n" +
                "\t\t  0% {}\n" +
                "\t\t}\n" +
                "\t\t@keyframes hd-quality {\n" +
                "\t\t  0% {\n" +
                "\t\t\t\n" +
                "\t\t\t  filter: blur(20px);\n" +
                "\t\t  }\n" +
                "\t\t  100% {\n" +
                "\t\t\t \n" +
                "\t\t\t  filter: blur(0px);\n" +
                "\t\t  }\n" +
                "\t\t}\n" +
                "\t\t.centered {\n" +
                "\t\t  position: absolute;\n" +
                "\t\t  top: 50%;\n" +
                "\t\t  left: 50%;\n" +
                "\t\t  transform: translate(-50%, -50%);\n" +
                "\t\t    -webkit-text-stroke-width: 0.2px;\n" +
                "\t\t\t-webkit-text-stroke-color: black;\n" +
                "\t\t}\n" +
                "\t\t.container {\n" +
                "\t\t  position: relative;\n" +
                "\t\t  text-align: center;\n" +
                "\t\t  color: white;\n" +
                "\t\t  font-size: 35px;\n" +
                "\t\t}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body><br>\n" +
                "\t\t<a href=\"https://revelogue.com/viet-lach\"><div class=\"container\">\n" +
                "\t\t  <img src=\"vietlach.png\" >\n" +
                "\t\t</div></a><br>\n" +
                "\t\t<a href=\"https://revelogue.com/van-hoc\"><div class=\"container\">\n" +
                "\t\t  <img src=\"vanhoc.png\" >\n" +
                "\t\t</div></a><br>\n" +
                "\t\t<a href=\"https://revelogue.com/dien-anh\"><div class=\"container\">\n" +
                "\t\t  <img src=\"dienanh.png\" >\n" +
                "\t\t</div></a><br>\n" +
                "\t\t<a href=\"https://revelogue.com/am-nhac\"><div class=\"container\">\n" +
                "\t\t  <img src=\"nhac.png\" >\n" +
                "\t\t</div></a><br>\n" +
                "\t\t<a href=\"https://revelogue.com/nhiep-anh\"><div class=\"container\">\n" +
                "\t\t  <img src=\"nhiepanh.png\" >\n" +
                "\t\t</div></a><br>\n" +
                "\t\t<a href=\"https://revelogue.com/van-hoa\"><div class=\"container\">\n" +
                "\t\t  <img src=\"vanhoa.png\" >\n" +
                "\t\t</div></a><br>\n" +
                "\t\t<a href=\"https://revelogue.com/my-thuat\"><div class=\"container\">\n" +
                "\t\t  <img src=\"mythuat.png\" >\n" +
                "\t\t</div></a>\n" +
                "\t</body>\n" +
                "</html>";
        if (isConnected()){

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                trustAllHosts();
                view.loadDataWithBaseURL("file:///android_res/drawable/", yourData, "text/html", "utf-8", null);

                // view.loadData(yourData, "text/html", "UTF-8");

            } else {
                // view.loadData(yourData, "text/html", "UTF-8");
                view.loadDataWithBaseURL("file:///android_res/drawable/", yourData, "text/html", "utf-8", null);

            }

        } else {
            Toast.makeText(this, R.string.nointernet, Toast.LENGTH_SHORT).show();

            img.setImageResource(R.drawable.nointernet);
            container2.stopShimmer();
            reload_auto();

        }
    }

    public void reload_auto() {
        // Create the Handler object (on the main thread by default)
        Handler handler = new Handler();
// Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Handlers", "Called on main thread");

                if (isConnected()){
                    img.setImageResource(R.drawable.logo);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                        trustAllHosts();
                        view.reload();
                    } else {
                        view.reload();
                    }
                    // handler.removeCallbacks(runnableCode);
                } else {
                    handler.postDelayed(this, 5000);
                }
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object

            }
        };
// Start the initial runnable task by posting through the handler
        handler.post(runnableCode);
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** public void loadFragment(Fragment fragment) {
         FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
         transaction.replace(R.id.frame_container, fragment);
         transaction.commit();
     }**/

}