package pm.yan.beetsync;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yann on 18/1/2015.
 */
public class QueueDownloadTask extends AsyncTask<JSONArray, Integer, List<Long>> {
    private static final String TAG = "Q Donwload";
    private Context mcontext;
    private ProgressBar qprogress;
    private ProgressBar dlprogress;
    public static List<Long> dl_ids;

    public QueueDownloadTask(Context mcontext, ProgressBar qprogress, ProgressBar dlprogress) {
        this.mcontext = mcontext;
        this.qprogress = qprogress;
        this.dlprogress = dlprogress;
        dl_ids = new ArrayList<Long>();
    }

    @Override
    protected List<Long> doInBackground(JSONArray... params) {
        JSONArray items = params[0];

        File downloaddir = new File(Environment.DIRECTORY_MUSIC+"/beets/"); //TODO: let the people choose you nazi
        downloaddir.mkdir();

        DownloadManager dl = (DownloadManager) mcontext.getSystemService(mcontext.DOWNLOAD_SERVICE);

        for(int n = 0; n < items.length(); n++)
        {
            JSONObject object = null;
            try {
                object = items.getJSONObject(n);
                String name = object.getString("title")+" - "+object.getString("artist");
                String file_name = name + "." + object.getString("format");
                File file = new File(downloaddir.toString() + "/" + file_name);

                if(!file.exists()) {
                    Uri url_file = Uri.parse(MyMain.BASE_URL + "/" + Integer.toString(object.getInt("id")) + "/file");
                    DownloadManager.Request rq = new DownloadManager.Request(url_file);
                    if (MyMain.SSLEnable) {
                        rq.addRequestHeader("Authorization", String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", MyMain.UNAME, MyMain.PASS).getBytes(), Base64.DEFAULT)));
                    }
                    rq.setDestinationInExternalPublicDir(downloaddir.toString(), file_name);
                    rq.setTitle(name);
                    rq.setDescription("Beetsync download " + name);
                    rq.allowScanningByMediaScanner();
                    rq.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                    rq.setVisibleInDownloadsUi(false);
                    dl_ids.add(dl.enqueue(rq));
                }
                publishProgress(n);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        publishProgress(items.length());
        return dl_ids;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        qprogress.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(List<Long> dl_ids) {
        super.onPostExecute(dl_ids);
    }
}
