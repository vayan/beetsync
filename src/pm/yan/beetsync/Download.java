package pm.yan.beetsync;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yann on 18/1/2015.
 */
public class Download extends Activity {
    private static final String TAG = "Download";
    private TextView totalItems;
    private TextView totalSize;
    private Button ButtonStartDownload;
    private ProgressBar dlprogress;
    private JSONArray items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String data = MyMain.DATA_JSON;

        try {
            JSONObject obj = new JSONObject(data);
            items = obj.getJSONArray("items");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String totalitems = Integer.toString(items.length()) + " Items";
        String totalsize = "0 Mo";
        try {
            totalsize = calculateSize() + " Mo";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProgressBar connectProgress = (ProgressBar) findViewById(R.id.progressBarConnecting);
        connectProgress.setVisibility(View.INVISIBLE);
        setContentView(R.layout.download);

        ButtonStartDownload = (Button) findViewById(R.id.buttonStartDownload);
        totalItems = (TextView) findViewById(R.id.textTotalItems);
        totalSize = (TextView) findViewById(R.id.textTotalSize);
        dlprogress = (ProgressBar) findViewById(R.id.progressBarAll);

        dlprogress.setMax(items.length());
        totalItems.setText(totalitems);
        totalSize.setText(totalsize);
    }

    public void onDownloadClicked(View view) {
        ButtonStartDownload.setEnabled(false);

        BcastReceiver onDownloadComplete = new BcastReceiver(dlprogress);
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        new QueueDownloadTask(this).execute(items);

        Log.d(TAG, "out of queuing loop");
    }

    private String calculateSize() throws JSONException {
        double totalSize = 0;
        for(int n = 0; n < items.length(); n++)
        {
            JSONObject object = items.getJSONObject(n);
            totalSize += object.getDouble("size");
        }
        Double mosize = (totalSize / 1024) / 1024;
        return mosize.toString();
    }
}
