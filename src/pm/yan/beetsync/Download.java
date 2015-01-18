package pm.yan.beetsync;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * Created by yann on 18/1/2015.
 */
public class Download extends Activity {

    private static final String TAG = "Download";
    private TextView totalItems;
    private TextView totalSize;
    private JSONArray items;
    private String path = Environment.DIRECTORY_MUSIC; //TODO: let the people choose you nazi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
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

        setContentView(R.layout.download);

        totalItems = (TextView) findViewById(R.id.textTotalItems);
        totalSize = (TextView) findViewById(R.id.textTotalSize);

        totalItems.setText(totalitems);
        totalSize.setText(totalsize);



    }

    public void onDownloadClicked(View view) {
        File downloaddir = new File(path+"/beets/");
        downloaddir.mkdir();

        DownloadManager dl = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        for(int n = 0; n < items.length(); n++)
        {
            JSONObject object = null;
            try {
                object = items.getJSONObject(n);
                Uri url_file = Uri.parse(MyMain.BASE_URL + "/" + Integer.toString(object.getInt("id")) + "/file");
                DownloadManager.Request rq = new DownloadManager.Request(url_file);
                rq.addRequestHeader("Authorization", String.format("Basic %s", Base64.encodeToString(
                        String.format("%s:%s", MyMain.UNAME, MyMain.PASS).getBytes(), Base64.DEFAULT)));
                rq.allowScanningByMediaScanner();
                rq.setTitle("BeetSync App");
                rq.setDescription("Downloading " + object.getString("title") + "of " + object.getString("artist"));
                rq.setDestinationInExternalPublicDir(downloaddir.toString(), object.getString("title")+object.getString("artist")+"."+object.getString("format"));
                rq.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                dl.enqueue(rq);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
