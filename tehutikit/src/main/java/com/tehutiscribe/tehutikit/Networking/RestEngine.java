package com.tehutiscribe.tehutikit.Networking;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class RestEngine {

    private RestCallback callback;
    private HttpRequestBase request;
    private String url;
    private RestTask task;

    public static Builder newInstance(HttpType type) {
        return new Builder(type);
    }

    public static class Builder {

        private RestCallback callback;
        private HttpRequestBase request;
        private String url;
        private RestTask task;

        public Builder(HttpType type) {
            switch (type) {
                case GET:
                    request = new HttpGet();
                    break;
                case POST:
                    request = new HttpPost();
                    break;
                case PUT:
                    request = new HttpPut();
                    break;
                case DELETE:
                    request = new HttpDelete();
                    break;
            }
        }

        public Builder setCallback(RestCallback callback) {
            this.callback = callback;
            return this;
        }


        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * Set Url with argument to be formatted into the string.
         * Format String url as 'http://www.example.com/users/%s/edit'
         * Where '%s' will be replaced by arg.
         *
         * @param url - Url endpoint
         * @param arg - Argument to inject into url, typically and ID of a database model
         */
        public Builder setUrlWithArgument(String url, Object arg) {
            this.url = String.format(url, String.valueOf(arg));
            return this;
        }

        public Builder setJsonEntity(String jsonString) {
            try {
                StringEntity entity = new StringEntity(jsonString);
                request.addHeader("Content-type", "application/json");

                if (request instanceof HttpPost) {
                    ((HttpPost) request).setEntity(entity);
                } else if (request instanceof HttpPut) {
                    ((HttpPut) request).setEntity(entity);
                } else {
                    throw new Exception("Request Type does not utilize entities");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder setEntity(ArrayList<NameValuePair> params) {

            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);

                if (request instanceof HttpPost) {
                    ((HttpPost) request).setEntity(entity);
                } else if (request instanceof HttpPut) {
                    ((HttpPut) request).setEntity(entity);
                } else {
                    throw new Exception("Request Type does not utilize entities");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder execute() {
            try {
                request.setURI(new URI(url));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            task = new RestTask();
            task.setResponseCallback(new RestTask.RestResponseCallback() {
                @Override
                public void onSuccess(String response) throws IOException {
                    callback.onSuccess(response);
                }

                @Override
                public void onError(Exception error, int statusCode) {
                    callback.onError(error.getMessage(), statusCode);
                }
            });
            task.execute(request);
            return null;
        }
    }
}
