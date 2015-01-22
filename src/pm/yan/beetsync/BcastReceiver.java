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
    private Context mcontext;


    public BcastReceiver(ProgressBar pbar, Context mcontext) {
        this.mcontext = mcontext;
        this.pbar = pbar;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Long dwnId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

        if (Download.dl_ids.contains(dwnId)) {
            Log.d(TAG, "id present in the list ! download finished");
            pbar.incrementProgressBy(1);
        }
    }
}
