package pm.yan.beetsync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class MyMain extends Activity {
    /**
     * Called when the activity is first created.
     */
    public  static String DATA_JSON = "";
    private static final String TAG = "MyMain";
    private TextView hostname;
    private TextView port;
    private TextView username;
    private TextView password;
    private boolean AuthEnable = false;
    private boolean SSLEnable = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        hostname = (TextView) findViewById(R.id.hostInput);
        port = (TextView) findViewById(R.id.portInput);
        username = (TextView) findViewById(R.id.usernameInput);
        password = (TextView) findViewById(R.id.passwordInput);
    }

    public void onConnectClicked(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String uri_protocol = SSLEnable ? "https://" : "http://";
        String uri_port = SSLEnable ? "443" : port.getText().toString();
        String url = uri_protocol + hostname.getText().toString() + ":" + uri_port + "/item";

        Response.Listener resp = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.v(TAG, "good rquest");
                Intent intent = new Intent(MyMain.this, Download.class);
                DATA_JSON = (String) response;
                startActivity(intent);
            }
        };

        Response.ErrorListener err_resp = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(TAG, "error rquest");
            }
        };

        StringRequest stringRequest = new StringRequestAuth(
                Request.Method.GET,
                url,
                resp,
                err_resp,
                username.getText().toString(),
                password.getText().toString(),
                AuthEnable);
        queue.add(stringRequest);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.authCheckbox:
                username.setEnabled(checked);
                password.setEnabled(checked);
                AuthEnable = checked;
                break;
            case R.id.sslCheckbox:
                SSLEnable = checked;
                break;
        }
    }
}
