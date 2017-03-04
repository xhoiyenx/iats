package com.hoiyen.iats.library;

import android.net.Uri;
import android.util.ArrayMap;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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
    public static void LoginRequest(final String url, final String username, final String password, final String fcm_token, final Listener<JSONObject> listener) {

        Uri builder = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("username", username)
                .appendQueryParameter("password", password)
                .appendQueryParameter("fcm_token", fcm_token)
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
     * Request member details
     */
    public static void ProfileRequest(final Listener<JSONObject> listener) {
        String url = "http://api.indonesiatrimmer.com/user/profile";
        SendRequest(url, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onResponse(response);
            }

            @Override
            public void onErrorResponse(String response) {
                listener.onErrorResponse(response);
            }
        });
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

    public static void SendRequest(final String url, final Listener<JSONObject> listener) {

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
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

        IATS.getInstance().addToRequestQueue(request);

    }

    private static String getErrorMessage(VolleyError error) {

        final NetworkResponse response = error.networkResponse;

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {


        } else if (error instanceof AuthFailureError) {

            if (response != null) {
                String json = new String(response.data);
                try {
                    JSONObject data = new JSONObject(json);
                    if (data.has("error")) {
                        return data.optString("error");
                    }
                }
                catch (JSONException ex) {

                }
            }

        } else if (error instanceof ServerError) {
            //TODO
        } else if (error instanceof NetworkError) {
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
        }

        return error.getMessage();

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
