package com.tehutiscribe.tehutikit.Networking;

public interface RestCallback {
    void onSuccess(String response);

    void onError(String error, int statusCode);
}
