package pm.yan.beetsync;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yann on 18/1/2015.
 */
public class Download extends Activity {
    private static final String TAG = "Download";
    public static List<Long> dl_ids = new ArrayList<>();
    private TextView totalItems;
    private TextView totalSize;
    private Button ButtonStartDownload;
    private ProgressBar dlprogress;
    private ProgressBar qprogress;
    private JSONArray items;
    private BcastReceiver onDownloadComplete;
    private QueueDownloadTask QueueTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "App created");

        setContentView(R.layout.download);
        String data = MyMain.DATA_JSON;

        try {
            JSONObject obj = new JSONObject(data);
            items = obj.getJSONArray("items");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String totalitems = Integer.toString(items.length()) + " Musics";
        String totalsize = "0 Mo";
        try {
            totalsize = calculateSize() + " Mo";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ButtonStartDownload = (Button) findViewById(R.id.buttonStartDownload);
        totalItems = (TextView) findViewById(R.id.textTotalItems);
        totalSize = (TextView) findViewById(R.id.textTotalSize);
        dlprogress = (ProgressBar) findViewById(R.id.progressBarAll);
        qprogress = (ProgressBar) findViewById(R.id.progressBarQ);

        dlprogress.setMax(items.length());
        qprogress.setMax(items.length());
        totalItems.setText(totalitems);
        totalSize.setText(totalsize);
        onDownloadComplete = new BcastReceiver(dlprogress, this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "App restart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "App start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "App stop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "App resumed");
        new WatchDownloadTask(this, dlprogress, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "App paused");
        unregisterReceiver(onDownloadComplete);
    }

    public void onCancelClicked(View view) {
        Log.d(TAG, "Interupt clicked");
        new WatchDownloadTask(this, dlprogress, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        QueueTask.cancel(true);
        qprogress.setProgress(0);
    }

    public void onDownloadClicked(View view) {
        ButtonStartDownload.setEnabled(false);
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        QueueTask = new QueueDownloadTask(this, qprogress, dlprogress);
        QueueTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, items);
    }

    private String calculateSize() throws JSONException {
        double totalSize = 0;
        for(int n = 0; n < items.length(); n++)
        {
            JSONObject object = items.getJSONObject(n);
            totalSize += object.getDouble("size");
        }
        Double mosize = (totalSize / 1024) / 1024;
        return Integer.toString(mosize.intValue());
    }
}
