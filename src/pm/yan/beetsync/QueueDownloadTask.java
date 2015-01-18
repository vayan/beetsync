package pm.yan.beetsync;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by yann on 18/1/2015.
 */
public class QueueDownloadTask extends AsyncTask<JSONArray, Integer, Void> {
    Context mcontext;

    public QueueDownloadTask(Context mcontext) {
        this.mcontext = mcontext;
    }

    @Override
    protected Void doInBackground(JSONArray... params) {
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
                Uri url_file = Uri.parse(MyMain.BASE_URL + "/" + Integer.toString(object.getInt("id")) + "/file");
                DownloadManager.Request rq = new DownloadManager.Request(url_file);
                rq.addRequestHeader("Authorization", String.format("Basic %s", Base64.encodeToString(
                        String.format("%s:%s", MyMain.UNAME, MyMain.PASS).getBytes(), Base64.DEFAULT)));
                rq.setDestinationInExternalPublicDir(
                        downloaddir.toString(),
                        name + "." + object.getString("format"));
                rq.setTitle("Downloading " + name);
                rq.setDescription("Downloading " + name);
                rq.allowScanningByMediaScanner();
                rq.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                dl.enqueue(rq);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
