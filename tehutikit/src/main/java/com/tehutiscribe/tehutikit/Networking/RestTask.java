package com.tehutiscribe.tehutikit.Networking;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class RestTask extends AsyncTask<HttpUriRequest, Void, Object> {
    private static final String TAG = "RestTask";


    public interface RestResponseCallback {
        public void onSuccess(String response) throws IOException;

        public void onError(Exception error, int statusCode);
    }

    private HttpClient mClient;
    private RestResponseCallback mCallback;
    private int httpStatusCode;

    public RestTask() {
        this(new DefaultHttpClient());
    }

    public RestTask(AbstractHttpClient client) {
        mClient = client;
    }

    public void setResponseCallback(RestResponseCallback callback) {
        mCallback = callback;
    }

    @Override
    protected Object doInBackground(HttpUriRequest... params) {
        HttpResponse serverResponse = null;
        String response;
        try {
            HttpUriRequest request = params[0];
            serverResponse = mClient.execute(request);

            BasicResponseHandler handler = new BasicResponseHandler();
            response = handler.handleResponse(serverResponse);
            httpStatusCode = serverResponse.getStatusLine().getStatusCode();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            if (serverResponse != null) {
                httpStatusCode = serverResponse.getStatusLine().getStatusCode();
            }
            return e;
        }

        return response;
    }

    @Override
    protected void onPostExecute(Object result) {
        try {
            if (httpStatusCode == 200 || httpStatusCode == 201) {
                mCallback.onSuccess((String) result);
            } else if (result instanceof Exception) {
                mCallback.onError((Exception) result, httpStatusCode);
            } else {
                mCallback.onError(null, httpStatusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
        mCallback.onError(null, httpStatusCode);
    }
}
