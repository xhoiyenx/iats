package com.hoiyen.iats.library;

import android.net.Uri;
import android.util.ArrayMap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hoiyen.iats.utils.IATS;
import com.hoiyen.iats.utils.PostRequest;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class ApiRequest {

    /**
     * Login request
     */
    public static void LoginRequest(final String url, final String username, final String password, final Listener<JSONObject> listener) {

        Uri builder = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("username", username)
                .appendQueryParameter("password", password)
                .build();

        JsonObjectRequest request = new JsonObjectRequest(builder.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = getErrorMessage(error);
                listener.onErrorResponse(errorMessage);
            }
        });

        IATS.getInstance().addToRequestQueue(request);

    }

    /**
     * Registration request
     */
    public static void RegisterRequest(final String url, Map<String, String> params, final Listener<JSONObject> listener) {
        PostRequest request = new PostRequest(url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = getErrorMessage(error);
                listener.onErrorResponse(errorMessage);
            }
        });

        IATS.getInstance().addToRequestQueue(request);
    }

    /**
     * Post share request
     */
    public static void PostRequest(final String url, final Map<String, String> params, final Listener<JSONObject> listener) {
        PostRequest request = new PostRequest(url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = getErrorMessage(error);
                listener.onErrorResponse(errorMessage);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer ".concat(Prefs.getString("token", "")));
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        IATS.getInstance().addToRequestQueue(request);
    }

    private static String getErrorMessage(VolleyError error) {
        NetworkResponse response = error.networkResponse;
        String json = new String(response.data);
        try{
            JSONObject obj = new JSONObject(json);
            return obj.getString("error");
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Listener
     * @param <T>
     */
    public interface Listener<T> {
        void onResponse(T response);
        void onErrorResponse(String response);
    }

}
