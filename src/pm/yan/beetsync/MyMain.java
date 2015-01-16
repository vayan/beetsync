package pm.yan.beetsync;

import android.app.Activity;
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

public class MyMain extends Activity {
    /**
     * Called when the activity is first created.
     */
    private static final String TAG = "MyMain";
    private TextView hostname;
    private TextView port;
    private TextView username;
    private TextView password;

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
        
        String url ="http://www.google.com";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.v(TAG, "good rquest");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(TAG, "error rquest");
            }
        });
        queue.add(stringRequest);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.authCheckbox:
                username.setEnabled(checked);
                password.setEnabled(checked);
                break;
            case R.id.sslCheckbox:
                break;
        }
    }
}
