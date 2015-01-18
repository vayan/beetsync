package pm.yan.beetsync;

import android.util.Base64;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yann on 16/1/2015.
 */
public class StringRequestAuth extends StringRequest {
    private String username;
    private String password;

    public StringRequestAuth(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, String user, String pass, boolean authEnable) {
        super(method, url, listener, errorListener);
        username = authEnable ? user : "";
        password = authEnable ? pass : "";
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(
                "Authorization",
                String.format("Basic %s", Base64.encodeToString(
                        String.format("%s:%s", username, password).getBytes(), Base64.DEFAULT)));
        return params;
    }
}
