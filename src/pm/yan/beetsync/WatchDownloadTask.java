package pm.yan.beetsync;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import org.json.JSONArray;

import java.util.List;

/**
 * Created by yann on 22/1/2015.
 */
public class WatchDownloadTask extends AsyncTask<Void, Integer, Void> {
    private Context mcontext;
    private ProgressBar dl_progress;
    private Boolean interupt;

    public WatchDownloadTask(Context mcontext, ProgressBar dl_progress, Boolean interupt) {
        this.mcontext = mcontext;
        this.dl_progress = dl_progress;
        this.interupt = interupt;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DownloadManager dl = (DownloadManager) mcontext.getSystemService(mcontext.DOWNLOAD_SERVICE);

        List<Long> dl_ids = QueueDownloadTask.dl_ids;
        if (dl_ids.size() > 0) {
            Integer finished = 0;
            for (Long id : dl_ids) {
                if (interupt) {
                    dl.remove(id);
                } else {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(id);
                    Cursor c = dl.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == columnIndex) {
                            finished++;
                        }
                    }
                }

            }
            publishProgress(finished);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        dl_progress.setProgress(values[0]);
    }


}
