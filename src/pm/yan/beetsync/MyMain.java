package pm.yan.beetsync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    public static String DATA_JSON = "";
    public static String BASE_URL = "";
    public static String UNAME = "";
    public static String PASS = "";
    private static final String TAG = "MyMain";
    private TextView hostname;
    private TextView port;
    private TextView username;
    private TextView password;
    private boolean AuthEnable = false;
    private boolean SSLEnable = false;


    @Override
    protected void onResume() {
        super.onResume();
        onCheckboxClicked(findViewById(R.id.authCheckbox));
    }

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
        build_url();
        RequestQueue queue = Volley.newRequestQueue(this);
        Response.Listener resp = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                DATA_JSON = (String) response; //too big to be passed with intent extra
                Intent intent = new Intent(MyMain.this, Download.class);
                startActivity(intent);
            }
        };

        Response.ErrorListener err_resp = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO: handle error
            }
        };

        UNAME =  username.getText().toString();
        PASS = password.getText().toString();

        StringRequest stringRequest = new StringRequestAuth(
                Request.Method.GET,
                BASE_URL,
                resp,
                err_resp,
                UNAME,
                PASS,
                AuthEnable);
        queue.add(stringRequest);
    }

    public void build_url() {
        String uri_protocol = SSLEnable ? "https://" : "http://";
        String uri_port = SSLEnable ? "443" : port.getText().toString();
        BASE_URL = uri_protocol + hostname.getText().toString() + ":" + uri_port + "/item";
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
