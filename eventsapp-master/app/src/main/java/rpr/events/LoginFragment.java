package rpr.events;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


// Fragment de connexion utilisateur
public class LoginFragment extends Fragment {


    EditText username;
    EditText password;
    Button forgotPassword;
    Button login;
    UserSessionManager session;
    RequestQueue queue;


    private static String textUsername;
    float v = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_fragment,container,false);


        username = root.findViewById(R.id.etUsername);
        password = root.findViewById(R.id.etPassword);
        forgotPassword = root.findViewById(R.id.forgotpassword);
        login = root.findViewById(R.id.button);


        username.setTranslationY(800);
        password.setTranslationY(800);
        forgotPassword.setTranslationY(200);
        login.setTranslationY(800);

        username.setAlpha(v);
        password.setAlpha(v);
        forgotPassword.setAlpha(v);
        login.setAlpha(v);

        username.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgotPassword.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

        session = new UserSessionManager(getActivity().getApplicationContext());
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());


        if (session.isUserLoggedIn()){
            Intent intent = new Intent(getContext(), Navigation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(username.getText().toString().trim().equals("")){
                    Snackbar.make(v, "Please specify Username", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    if (password.getText().toString().equals("")) {
                        Snackbar.make(v, "Please specify Password", Snackbar.LENGTH_SHORT).show();
                    } else {
                        final String etUsername = username.getText().toString().trim();
                        final String etPassword = password.getText().toString();


                        StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/Login3.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        UserSessionManager session = new UserSessionManager(getActivity().getApplicationContext());
                                        session.createUserLoginSession(jsonResponse.getString("pseudo"), jsonResponse.getString("email"), jsonResponse.getInt("id"), jsonResponse.getInt("usertype_id"), jsonResponse.getString("usertype"),jsonResponse.getInt("usertypes"));
                                        // génération d'un token après le succès d'une connexion via le service FirebaseMessaging
                                        FirebaseMessaging.getInstance().subscribeToTopic("0");
                                        for (int i=1; i<jsonResponse.getInt("usertypes");i++){
                                            if (i==jsonResponse.getInt("usertype_id")){
                                                FirebaseMessaging.getInstance().subscribeToTopic(i+""); }
                                            else{
                                                FirebaseMessaging.getInstance().unsubscribeFromTopic(i+"");
                                            }
                                        }
                                        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.firebase_pref),MODE_PRIVATE);
                                        final int user_id = jsonResponse.getInt("id");
                                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                if (!task.isSuccessful()) {
                                                    return;
                                                }
                                                // Génération d'un Token
                                                String t = task.getResult();
                                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                                edit.clear();
                                                edit.commit();
                                                edit.putString(getString(R.string.firebase_token),t);
                                                edit.commit();
                                                System.out.println("GEN TOKEN : " + t);
                                            }
                                        });
                                        final String token = sharedPreferences.getString(getString(R.string.firebase_token),"token");
                                        System.out.println("MON TOKEN EST : " + token);
                                        StringRequest fcmRequest = new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/updateFCMtoken.php", new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    System.out.println(jsonResponse);

                                    if (success) {

                                    } else {
                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }

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
                                                params.put("user_id", user_id+"");
                                                params.put("token", token);

                                                return params;
                                            }

                                        };

                                        queue.add(fcmRequest);


                                        Toast.makeText(getActivity().getApplicationContext(), "Login Successful " + jsonResponse.getString("pseudo"), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), Navigation.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            }, new Response.ErrorListener()
                        {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Snackbar.make(v, "Couldn't connect to internet",Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        ) {

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("pseudo", etUsername);
                                params.put("motdepasse", etPassword);
                                return params;
                            }
                        };
                        queue.add(loginRequest);
                    }
                }
            }
        });




        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), ForgotPasswordActivity.class));
            }
        });

        return root;


    }
    public static String getPseudo(){
        return textUsername;
    }



}
