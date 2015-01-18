package pm.yan.beetsync;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by yann on 18/1/2015.
 */
public class BcastReceiver extends BroadcastReceiver {
    private static final String TAG = "Broad";
    private ProgressBar pbar;

    public BcastReceiver(ProgressBar pbar) {
        this.pbar = pbar;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Long dwnId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        Log.d(TAG, "receive broadcast");
        pbar.incrementProgressBy(1);
    }
}
