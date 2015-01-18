package pm.yan.beetsync;

import android.os.AsyncTask;

import java.net.URL;

/**
 * Created by yann on 18/1/2015.
 */
public class DownloadAsyncFile extends AsyncTask <URL, Integer, Long> {
    protected Long doInBackground(URL... urls) {
        int count = urls.length;
        long totalSize = 0;
        for (int i = 0; i < count; i++) {
            publishProgress((int) ((i / (float) count) * 100));
            if (isCancelled()) break;
        }
        return totalSize;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(Long result) {
    }
}
