package rpr.events;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// Activité permettant la modification du mot de passe d'un profil utilisateur (User et Provider)
public class changePassword extends AppCompatActivity {

    EditText oldPassword;
    EditText newPassword;
    EditText newPassword2;
    Button changePassword;
    UserSessionManager session;
    RequestQueue queue;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        session = new UserSessionManager(this);
        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(UserSessionManager.KEY_EMAIL);
        oldPassword = (EditText) findViewById(R.id.etoldPassword);
        newPassword = (EditText) findViewById(R.id.etnewPassword);
        newPassword2 = (EditText) findViewById(R.id.etnewPassword2);
        changePassword = (Button) findViewById(R.id.bchangePassword);
        queue = Volley.newRequestQueue(getApplicationContext());

        newPassword.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (newPassword.getText().toString().equals(newPassword2.getText().toString())){
                    newPassword2.setError(null);
                }
                else{
                    newPassword2.setError("Passwords don't match");
                }

            }
        });

        newPassword2.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (newPassword.getText().toString().equals(newPassword2.getText().toString())){
                    newPassword2.setError(null);
                }
                else{
                    newPassword2.setError("Passwords don't match");
                }

            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                // Vérification de la corresponsance des mots de passe

                if(oldPassword.getText().toString().equals("")){
                    Snackbar.make(v, "Your old password is missing !", Snackbar.LENGTH_SHORT).show();
                }

                else if (!newPassword2.getText().toString().equals(newPassword.getText().toString())) {
                    Snackbar.make(v, "Your new password don't match with the old password !", Snackbar.LENGTH_SHORT).show();
                }
                else {
                        final String oldP = oldPassword.getText().toString();
                        final String newP = newPassword.getText().toString();

                        StringRequest changePasswordRequest = new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/changePassword2.php",
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        try{
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            } else {

                                                Toast.makeText(getApplicationContext(), "Password couldn't be changed", Toast.LENGTH_SHORT).show();

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
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("email", email);
                                params.put("oldPassword", oldP);
                                params.put("newPassword", newP);

                                return params;
                            }

                        };

                        queue.add(changePasswordRequest);

                }

            }
        });


    }
}
