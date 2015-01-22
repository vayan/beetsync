package pm.yan.beetsync;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.Iterator;
import java.util.List;

/**
 * Created by yann on 22/1/2015.
 */
public class WatchDownloadTask extends AsyncTask<Void, Integer, Integer> {
    private static final String TAG = "watch download";
    private Context mcontext;
    private ProgressBar dl_progress;
    private Boolean interupt;

    public WatchDownloadTask(Context mcontext, ProgressBar dl_progress, Boolean interupt) {
        this.mcontext = mcontext;
        this.dl_progress = dl_progress;
        this.interupt = interupt;
        Log.d(TAG, "the progress was at " + Integer.toString(dl_progress.getProgress()));
    }

    public static long[] convertIntegers(List<Long> longs) {
        long[] ret = new long[longs.size()];

        Iterator<Long> iterator = longs.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next();
        }
        return ret;
    }


    @Override
    protected Integer doInBackground(Void... params) {

        DownloadManager dl = (DownloadManager) mcontext.getSystemService(mcontext.DOWNLOAD_SERVICE);

        Integer nb_downloaded = 0;
        if (Download.dl_ids.size() > 0) {
            long[] ids = convertIntegers(Download.dl_ids);

            if (interupt) {
                Integer nb_canceled = dl.remove(ids);
                Log.d(TAG, "Music removed : " + Integer.toString(nb_canceled));
            } else {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(ids);
                query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
                Cursor c = dl.query(query);
                nb_downloaded = c.getCount();
                Log.d(TAG, "For now there's music downloaded : " + Integer.toString(c.getCount()));
                c.close();
            }
        }
        publishProgress(nb_downloaded);
        return nb_downloaded;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        dl_progress.setProgress(values[0]);
    }



    @Override
    protected void onPostExecute(Integer nb_downloaded) {
        super.onPostExecute(nb_downloaded);
    }
}
