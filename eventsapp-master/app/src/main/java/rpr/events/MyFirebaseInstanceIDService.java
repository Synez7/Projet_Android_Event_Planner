package rpr.events;


import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;


import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseIIDService";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
    }


    private void onTokenRefresh() {
        // Get updated InstanceID token.
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if(!task.isSuccessful()){
                    Log.d(TAG, "Failed to get token ");
                }
                    String refreshedToken = task.getResult();
                    Log.d(TAG, "Refreshed token: " + refreshedToken);
                    sendRegistrationToServer(refreshedToken);
                    System.out.println("TOKEN GEN : " + refreshedToken);
                }
            });
        }



    private void sendRegistrationToServer(final String token) {
        // TODO: Implement this method to send token to your app server.

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.firebase_pref),MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
        edit.putString(getString(R.string.firebase_token),token);
        edit.commit();


        UserSessionManager session = new UserSessionManager(getApplicationContext());
        if (session.isUserLoggedIn()) {
            HashMap<String, String> user = session.getUserDetails();
            final String user_id = user.get(UserSessionManager.KEY_USER_ID);
            final String usertype_id = user.get(UserSessionManager.KEY_USERTYPE_ID);
            final String usertypes = user.get(UserSessionManager.KEY_USERTYPES);
            FirebaseMessaging.getInstance().subscribeToTopic("0");
            for (int i=1; i<Integer.parseInt(usertypes);i++){
                if (i==Integer.parseInt(usertype_id)){
                    FirebaseMessaging.getInstance().subscribeToTopic(i+"");
                }
                else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(i+"");
                }
            }
            FirebaseMessaging.getInstance().subscribeToTopic(user_id);
            StringRequest fcmRequest = new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/updateFCMtoken.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                      /*  try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            System.out.println("REPONSE FIREBASE : " + jsonResponse);

                            if (success) {

                            System.out.println("BINGO");

                            } else {


                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error

                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", user_id);
                    params.put("token", token);


                    return params;
                }

            };
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(fcmRequest);
        }
    }
}